package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Product implements Handler{
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
        if (parseString[0].equals("c")){
            SendMessage sendMessage = messageTemplate.category(user, Integer.parseInt(parseString[1]), Integer.parseInt(parseString[2]));

//            EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
//            editMessageReplyMarkup.setChatId(String.valueOf(user.getId()));
//            editMessageReplyMarkup.setMessageId(callback.getMessage().getMessageId());
//            editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());
//            messageTemplate.editText(user,sendMessage.getText(),callback.getMessage().getMessageId());
//            messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(),callback.getMessage().getMessageId());
            List<PartialBotApiMethod<? extends Serializable>> list = messageTemplate.editTextAndReplyMarkup(user, callback.getMessage().getMessageId(), sendMessage.getText(), (InlineKeyboardMarkup) sendMessage.getReplyMarkup());
//            return Collections.singletonList(messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(),callback.getMessage().getMessageId()));
            return list;
        }

        if (parseString[0].equals("p")){

            if (parseString.length>2&&(parseString[2].equals("minus")||parseString[2].equals("plus"))){
                return change(callback,user,parseString[2]);
            }

            return Collections.singletonList(messageTemplate.product(user,Integer.parseInt(parseString[1])));
        }

        return Collections.singletonList(messageTemplate.category(user,1,1));
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
