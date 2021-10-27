package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.model.order.Order;
import uz.java.maniac.asaxiy_bot.model.order.OrderState;
import uz.java.maniac.asaxiy_bot.repository.OrderRepository;
import uz.java.maniac.asaxiy_bot.repository.ProductRepository;
import uz.java.maniac.asaxiy_bot.repository.ProductWithAmountRepository;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;
import uz.java.maniac.asaxiy_bot.service.OrderService;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;
import uz.java.maniac.asaxiy_bot.translations.Translations;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Col;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static uz.java.maniac.asaxiy_bot.translations.Translations.MainMenuBtn;
import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createMessageTemplate;

@Component
public class OrderHandler implements Handler{

    @Autowired
    private MessageTemplate messageTemplate;
    @Autowired
    private StartHandler startHandler;
    @Autowired
    private UnirestHelper helper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductWithAmountRepository productWithAmountRepository;
    @Autowired
    private TelegramUserRepository userRepository;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {
        System.out.println(message);
        SendMessage sendMessage = createMessageTemplate(user);

        if (message.equals(Translations.CancelBtn.get(user))){
            orderService.removePhone(user);
            user.setState(State.START);
            userRepository.save(user);
            List<PartialBotApiMethod<? extends Serializable>> list=new ArrayList<>();
            list.add(messageTemplate.removeProcess(user));
            list.add(messageTemplate.mainMenu(user));
            return list;
        }
        List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user, OrderState.DRAFT);

        if (orders.size()==0){
            orderService.removePhone(user);
            user.setState(State.START);
            userRepository.save(user);
            List<PartialBotApiMethod<? extends Serializable>> list=new ArrayList<>();
            list.add(messageTemplate.removeProcess(user));
            list.add(messageTemplate.mainMenu(user));
            return list;
        }




        if (orders.get(0).getOrder_phone()!=null){

            if (orders.get(0).getOrder_location()!=null){
                user.setState(State.START);
                userRepository.save(user);
                orders.get(0).setOrderState(OrderState.ACTIVE);
                orderRepository.save(orders.get(0));
                Col col=new Col();
                sendMessage.setText(Translations.ConfirmOrderMsg.get(user));
                col.add(MainMenuBtn.get(user.getLang()),"EXIT");
                sendMessage.setReplyMarkup(col.getMarkup());
                return Collections.singletonList(sendMessage);
            }

            orders.get(0).setOrder_location(message);
            orderRepository.save(orders.get(0));
//            user.setState(State.START);
//            userRepository.save(user);
            List<PartialBotApiMethod<? extends Serializable>> list=new ArrayList<>();
            list.add(messageTemplate.removeProcess(user));
            list.add(messageTemplate.mainMenu(user));
            return list;

        } else {
            orders.get(0).setOrder_phone(message);
            orderRepository.save(orders.get(0));
//            orderService.removePhone(user);



            KeyboardButton button=new KeyboardButton();
            button.setRequestLocation(true);
            button.setText(Translations.SendLocationBtn.get(user));
            KeyboardRow row=new KeyboardRow();
            row.add(button);

            KeyboardButton button2=new KeyboardButton();
            button2.setText(Translations.CancelBtn.get(user));
            KeyboardRow row2=new KeyboardRow();
            row2.add(button2);

            List<KeyboardRow> list=new ArrayList<>();
            list.add(row);
            list.add(row2);

            sendMessage.setText(Translations.NeedLocationMsg.get(user));
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(list);
            keyboardMarkup.setResizeKeyboard(true);
            sendMessage.setReplyMarkup(keyboardMarkup);
//            orderService.removePhone(user);
            return Collections.singletonList(sendMessage);
        }


    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {
        String[] parseString = TelegramUtil.parseString(callback.getData());

        if (parseString[0].equals("order")){
            orderService.removePhone(user);
            user.setState(State.ORDER);
            userRepository.save(user);
            List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user, OrderState.DRAFT);
            if (orders.size()!=0){
                long orderId = Long.parseLong(parseString[1]);

                KeyboardButton button=new KeyboardButton();
                button.setRequestContact(true);
                button.setText(Translations.SendPhoneBtn.get(user));
                KeyboardRow row=new KeyboardRow();
                row.add(button);

                KeyboardButton button2=new KeyboardButton();
                button2.setText(Translations.CancelBtn.get(user));
                KeyboardRow row2=new KeyboardRow();
                row2.add(button2);

                List<KeyboardRow> list=new ArrayList<>();
                list.add(row);
                list.add(row2);


                SendMessage sendMessage = createMessageTemplate(user);
                sendMessage.setText(Translations.NeedPhoneMsg.get(user));
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(list);
                keyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(keyboardMarkup);
                return Collections.singletonList(sendMessage);
            }

        }

        return null;
    }

    @Override
    public State operatedBotState() {
        return State.ORDER;
    }

    @Override
    public List<String> operatedCallBackQuery(TelegramUser user) {
        List<String> result=new ArrayList<>();
        result.add(State.ORDER.name());
        result.add(State.ORDER_PROCESS.name());
        result.add("order");
        return result;
    }
}
