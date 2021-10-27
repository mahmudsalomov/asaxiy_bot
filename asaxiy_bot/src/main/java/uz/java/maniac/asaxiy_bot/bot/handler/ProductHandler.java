package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ProductHandler implements Handler{
    @Autowired
    private MessageTemplate messageTemplate;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {
        SendMessage sendMessage=new SendMessage();
        sendMessage.setText("a");
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(user.getId()));
        return Collections.singletonList(sendMessage);
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {
        String text=callback.getData();
        String[] parseString = TelegramUtil.parseString(text);



        String back_callback="EXIT";

//        if (callback.getData().equals(State.PRODUCT.name())){
//            back_callback="EXIT";
//        }

//        if (parseString[0].equals("b")){
//            String templateParent = messageTemplate.findParent(Integer.parseInt(parseString[1]));
//            SendMessage sendMessage = messageTemplate.category(user, Integer.parseInt(templateParent), Integer.parseInt(parseString[2]),back_callback);
//            if (callback.getMessage().getReplyMarkup().equals(sendMessage.getReplyMarkup())){
//                return Collections.singletonList(sendMessage);
//            }
//            EditMessageReplyMarkup replyMarkup = messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(), callback.getMessage().getMessageId());
//            return Collections.singletonList(replyMarkup);
//        }

        if (parseString[0].equals("c")||parseString[0].equals("b")){
            if (Objects.equals(parseString[1], "1")) back_callback="EXIT";
            else
                back_callback="b-"+messageTemplate.findParent(Integer.parseInt(parseString[1]))+"-1";

            System.out.println("BAAAAACK = "+back_callback);

            SendMessage sendMessage = messageTemplate.category(user, Integer.parseInt(parseString[1]), Integer.parseInt(parseString[2]),back_callback);

//            EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
//            editMessageReplyMarkup.setChatId(String.valueOf(user.getId()));
//            editMessageReplyMarkup.setMessageId(callback.getMessage().getMessageId());
//            editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());
//            messageTemplate.editText(user,sendMessage.getText(),callback.getMessage().getMessageId());
//            messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(),callback.getMessage().getMessageId());
//            List<PartialBotApiMethod<? extends Serializable>> list = messageTemplate.editTextAndReplyMarkup(user, callback.getMessage().getMessageId(), sendMessage.getText(), (InlineKeyboardMarkup) sendMessage.getReplyMarkup());
//            return Collections.singletonList(messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(),callback.getMessage().getMessageId()));
            if (callback.getMessage().getReplyMarkup().equals(sendMessage.getReplyMarkup())){
                return Collections.singletonList(sendMessage);
            }
            EditMessageReplyMarkup replyMarkup = messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(), callback.getMessage().getMessageId());
            return Collections.singletonList(replyMarkup);
        }

        if (parseString[0].equals("p")){


            if (parseString.length>2&&(parseString[2].equals("minus")||parseString[2].equals("plus"))){
                return change(callback,user,parseString[2]);
            }

            return Collections.singletonList(messageTemplate.product(user,Integer.parseInt(parseString[1])));
        }

        return Collections.singletonList(messageTemplate.category(user,1,1,back_callback));
    }

    @Override
    public State operatedBotState() {
        return State.PRODUCT;
    }

    @Override
    public List<String> operatedCallBackQuery(TelegramUser user) {
        List<String> result=new ArrayList<>();
        result.add(State.PRODUCT.name());
        result.add("c");
        result.add("p");
        result.add("b");
        return result;
    }


    protected List<PartialBotApiMethod<? extends Serializable>> change(CallbackQuery callback, TelegramUser user,String command){

//            EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
//            editMessageReplyMarkup.setChatId(String.valueOf(user.getId()));
//            editMessageReplyMarkup.setMessageId(callback.getMessage().getMessageId());



            System.out.println(callback.getMessage().getReplyMarkup());

            InlineKeyboardMarkup markup=callback.getMessage().getReplyMarkup();
            String text = markup.getKeyboard().get(0).get(1).getText();
            String callbackData = markup.getKeyboard().get(1).get(0).getCallbackData();
            String [] s=callbackData.split("-");
            System.out.println(text);
            int quantity= Integer.parseInt(text);
            if (command.equals("minus")){
                if (quantity!=1){
                    quantity-=1;
                }

            }else {
                quantity+=1;
            }
            markup.getKeyboard().get(0).get(1).setText(String.valueOf(quantity));
            markup.getKeyboard().get(1).get(0).setCallbackData(s[0]+"-"+s[1]+"-"+quantity);
//            editMessageReplyMarkup.setReplyMarkup(markup);




            return Collections.singletonList(messageTemplate.editReplyMarkup(user,markup,callback.getMessage().getMessageId()));

    }

    public String parseString3th(String str){
        try {
            String[] parts = str.split("-");
            return parts[2];
        }catch (Exception e){
//            e.printStackTrace();
            return "";
        }
    }
}
