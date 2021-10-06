package uz.java.maniac.asaxiy_bot.model.message;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.Root;
import uz.java.maniac.asaxiy_bot.model.temp.RootModel;
import uz.java.maniac.asaxiy_bot.model.temp.TempRoot;
import uz.java.maniac.asaxiy_bot.service.Urls;
import uz.java.maniac.asaxiy_bot.translations.Translations;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Col;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Row;

import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createMessageTemplate;
import static uz.java.maniac.asaxiy_bot.translations.Translations.*;

@Component
public class MessageTemplate {
    @Autowired
    private TempRoot tempRoot;

    public SendMessage langChoice(TelegramUser user){
        try {
            Row row=new Row();
            row.add("\uD83C\uDDFA\uD83C\uDDFF O'z", Lang.OZ.name());
            row.add("\uD83C\uDDFA\uD83C\uDDFF Ўз", Lang.UZ.name());
            row.add("\uD83C\uDDF7\uD83C\uDDFA Ru", Lang.RU.name());
            SendMessage messageTemplate = createMessageTemplate(user);
            messageTemplate.setReplyMarkup(row.getMarkup());
            messageTemplate.setText("Tilni tanlang!\nТилни танланг!\nВыберите язык!");
            messageTemplate.enableMarkdown(true);
            return messageTemplate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public SendMessage mainMenu(TelegramUser user){
        try {
            if (user.getLang()!=null){
                Col col =new Col();
                col.add(Products.get(user),State.PRODUCT.name());
                col.add(Promotions.get(user),State.PROMOTIONS.name());
                col.add(Basket.get(user),State.BASKET.name());
                col.add(Profile.get(user),State.PROFILE.name());
                return SendMessage
                        .builder()
                        .chatId(String.valueOf(user.getId()))
                        .text(String.format("" + Menu.get(user), user.getId()))
                        .replyMarkup(col.getMarkup())
                        .build();
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public SendMessage category(TelegramUser user,int id){
        RootModel rootModel=tempRoot.get(user.getLang());
        if (rootModel==null) return langChoice(user);
        try {
            if (id==1){



                Col col=new Col();
                rootModel.getCategories().forEach(c->col.add(c.getName(),"c"+c.getId()));

                col.add("\uD83D\uDD19 Orqaga");
                col.add("\uD83C\uDFD8 Bosh sahifa","EXIT");
                return SendMessage
                        .builder()
                        .chatId(String.valueOf(user.getId()))
                        .text(String.format("test"))
                        .replyMarkup(col.getMarkup())
                        .build();
            }
            else{
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }




}
