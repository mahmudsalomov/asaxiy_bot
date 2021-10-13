package uz.java.maniac.asaxiy_bot.bot;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import uz.java.maniac.asaxiy_bot.bot.handler.Handler;
import uz.java.maniac.asaxiy_bot.bot.handler.Search;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;
import uz.java.maniac.asaxiy_bot.service.TelegramUserService;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class UpdateReceiver {
    private final List<Handler> handlers;
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramUserService telegramUserService;





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
    public UpdateReceiver(List<Handler> handlers, TelegramUserRepository telegramUserRepository, TelegramUserService telegramUserService) {
        this.handlers = handlers;
        this.telegramUserRepository = telegramUserRepository;
        this.telegramUserService = telegramUserService;
    }

    @Transactional
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (update.hasInlineQuery()){
                System.out.println(update.getInlineQuery());
                System.out.println("Query = "+update.getInlineQuery().getQuery());
                final Long chatId = update.getInlineQuery().getFrom().getId();

                final TelegramUser user = telegramUserRepository.findById(chatId)
                        .orElseGet(() -> telegramUserRepository.save(new TelegramUser(update.getMessage().getFrom())));
                Search search= (Search) getHandlerByState(State.SEARCH);
                return search.handle(user,update.getInlineQuery());
            }

            if (isMessageWithText(update)) {

                final Message message = update.getMessage();
                System.out.println(message);
//                update.getMessage().getFrom();


                final Long chatId = message.getFrom().getId();
                final TelegramUser user = telegramUserRepository.findById(chatId)
                        .orElseGet(() -> telegramUserRepository.save(new TelegramUser(update.getMessage().getFrom())));

                telegramUserRepository.save(user);
                System.out.println(handlers);
                System.out.println(handlers.size());
                handlers.forEach(System.out::println);
                handlers.forEach(h-> System.out.println(h.operatedCallBackQuery(user)));
//                System.out.println(user);
                Optional<TelegramUser> byId = telegramUserRepository.findById(user.getId());
//                System.out.println(byId.get());
                return getHandlerByState(user.getState()).handle(user, message.getText());
            }
            else if (update.hasCallbackQuery()) {

                System.out.println(update.getCallbackQuery().getData());
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                final Long chatId = callbackQuery.getFrom().getId();
                String message=callbackQuery.getData();
                System.out.println(update.getCallbackQuery().getData());
                if (callbackQuery.getData().equals("EXIT")){
                    final TelegramUser user = telegramUserRepository.findById(chatId)
                            .orElseGet(() -> telegramUserRepository.save(new TelegramUser(chatId)));
                    user.setState(State.START);
//                    user.setAction(" ");
                    telegramUserRepository.save(user);
                    return getHandlerByState(user.getState()).handle(user, callbackQuery.getData());
                }


                    final TelegramUser user = telegramUserRepository.findById(chatId)
                            .orElseGet(() -> telegramUserRepository.save(new TelegramUser(chatId)));
//                user.setAction(addAction(user, message).trim());
//                System.out.println(user.getAction());
//                User save = userRepository.save(user);
                return getHandlerByCallBackQuery(TelegramUtil.parseString(callbackQuery.getData(),0),user).handle(user, callbackQuery);
//
//                }




            }
//            else if (update.getMessage().hasContact()){
//                Start startHandler=new Start(telegramUserService);
//                final Long chatId =update.getMessage().getChatId();
//                final TelegramUser user = telegramUserRepository.findById(chatId)
//                        .orElseGet(() -> telegramUserRepository.save(new TelegramUser(chatId)));
//
//                return startHandler.addContact(user,update);
//            }

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
//        System.out.println("state Callback "+query);
//        System.out.println(handlers);
//        handlers.forEach(item-> System.out.println(item.operatedCallBackQuery()));
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

//    private String addAction(User user,String data){
//        String[] split = data.split("-");
//        if (user.getAction().equals("")){
//           switch (split[0]){
//               case "PRODUCT": return "-c-1";
//               case "catId": return "-c-"+split[1];
//               case "brandId": return "-b-"+split[1];
//               case "prodId": return "-p-"+split[1];
//               default:return " ";
//           }
//       }else  {
//            switch (split[0]){
//                case "PRODUCT": return user.getAction()+"-c-1";
//                case "catId": return user.getAction()+"-c-"+split[1];
//                case "brandId": return user.getAction()+"-b-"+split[1];
//                case "prodId": return user.getAction()+"-p-"+split[1];
//                default:return user.getAction()+" ";
//            }
//       }
//    }


}
