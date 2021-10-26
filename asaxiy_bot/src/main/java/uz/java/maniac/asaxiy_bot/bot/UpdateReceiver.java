package uz.java.maniac.asaxiy_bot.bot;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import uz.java.maniac.asaxiy_bot.bot.handler.Handler;
import uz.java.maniac.asaxiy_bot.bot.handler.OrderHandler;
import uz.java.maniac.asaxiy_bot.bot.handler.Search;
import uz.java.maniac.asaxiy_bot.bot.handler.Start;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.model.order.Order;
import uz.java.maniac.asaxiy_bot.model.order.OrderState;
import uz.java.maniac.asaxiy_bot.repository.OrderRepository;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;
import uz.java.maniac.asaxiy_bot.service.OrderService;
import uz.java.maniac.asaxiy_bot.service.TelegramUserService;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class UpdateReceiver {
    private final List<Handler> handlers;
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramUserService telegramUserService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final MessageTemplate messageTemplate;

    @Autowired
    private OrderHandler orderHandler;

    @Autowired
    private Search search;


    private List<PartialBotApiMethod<? extends Serializable>> handleIncomingInlineQuery(TelegramUser user, InlineQuery inlineQuery) {
        String query = inlineQuery.getQuery();
        System.out.println("Searching: " + query);
        if (!query.isEmpty()) {
            MessageTemplate messageTemplate=new MessageTemplate();
            return Collections.singletonList(messageTemplate.simple(user));
        } else {
            return null;
        }

    }





    @Autowired
    public UpdateReceiver(List<Handler> handlers, TelegramUserRepository telegramUserRepository, TelegramUserService telegramUserService, OrderRepository orderRepository, OrderService orderService, MessageTemplate messageTemplate) {
        this.handlers = handlers;
        this.telegramUserRepository = telegramUserRepository;
        this.telegramUserService = telegramUserService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.messageTemplate = messageTemplate;
    }

    @Transactional
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            List<PartialBotApiMethod<? extends Serializable>> results=new ArrayList<>();

            if (update.hasMessage()){

                final Message message = update.getMessage();
                System.out.println(message);
//                update.getMessage().getFrom();


                final Long chatId = message.getFrom().getId();
                final TelegramUser user = telegramUserRepository.findById(chatId)
                        .orElseGet(() -> telegramUserRepository.save(new TelegramUser(update.getMessage().getFrom())));
                if (isMessageWithText(update)) {



                    telegramUserRepository.save(user);
                    System.out.println(handlers);
                    System.out.println(handlers.size());
                    handlers.forEach(System.out::println);
                    handlers.forEach(h-> System.out.println(h.operatedCallBackQuery(user)));

                    if (user.getState().equals(State.SEARCH)) return search.handle(user,update);

                    Optional<TelegramUser> byId = telegramUserRepository.findById(user.getId());
                    return getHandlerByState(user.getState()).handle(user, message.getText());
                }

                if (update.getMessage().hasContact()){
                    List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user, OrderState.DRAFT);
                    if (orders.size()>0&&orders.get(0).getOrder_phone()==null)
                        return orderHandler.handle(user,update.getMessage().getContact().getPhoneNumber());
                }

                if (update.getMessage().hasLocation()){
                    List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user, OrderState.DRAFT);
                    if (orders.size()>0&&orders.get(0).getOrder_phone()==null)
                        return orderHandler.handle(user,update.getMessage().getLocation().toString());
                }
            }

            if (update.hasInlineQuery()){


                System.out.println(update.getInlineQuery());
                System.out.println("Query = "+update.getInlineQuery().getQuery());
                final Long chatId = update.getInlineQuery().getFrom().getId();

                final TelegramUser user = telegramUserRepository.findById(chatId)
                        .orElseGet(() -> telegramUserRepository.save(new TelegramUser(update.getInlineQuery().getFrom())));

                if (user.getState().equals(State.ORDER)) results.add(messageTemplate.removeProcess(user));
                Search search= (Search) getHandlerByState(State.SEARCH);
                results.addAll(search.handle(user,update.getInlineQuery()));
                return results;
            }

            else if (update.hasCallbackQuery()) {
                System.out.println(update.getCallbackQuery().getData());
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final Long chatId = callbackQuery.getFrom().getId();
                String message=callbackQuery.getData();
                System.out.println(update.getCallbackQuery().getData());
                final TelegramUser user = telegramUserRepository.findById(chatId)
                        .orElseGet(() -> telegramUserRepository.save(new TelegramUser(callbackQuery.getFrom())));
                if (user.getState().equals(State.ORDER)) results.add(messageTemplate.removeProcess(user));
                if (callbackQuery.getData().equals("EXIT")){
                    user.setState(State.START);
                    telegramUserRepository.save(user);

                    results.addAll(getHandlerByState(user.getState()).handle(user, callbackQuery.getData()));
                    return results;
                }
                results.addAll(getHandlerByCallBackQuery(TelegramUtil.parseString(callbackQuery.getData(),0),user).handle(user, callbackQuery));
                return results;
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException | IOException e) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByState(State state) {
//        System.out.println("state "+state);
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query,TelegramUser user) {
        String[] parts=query.split("-");
        String finalQuery = parts[0];
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery(user).stream()
                        .anyMatch(finalQuery::equals))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText()&& !update.getMessage().hasContact();
    }
}
