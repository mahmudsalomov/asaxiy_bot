package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.ProductSmall;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;
import uz.java.maniac.asaxiy_bot.translations.Translations;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Col;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class Search implements Handler{
    @Autowired
    private UnirestHelper helper;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {
        return null;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {
        return null;
    }


    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, InlineQuery inlineQuery) throws IOException{

        AnswerInlineQuery answer = new AnswerInlineQuery();
//        answer.se

        List<InlineQueryResult> results=new ArrayList<>();
        String[] parseString = parseString(inlineQuery.getQuery());

        List<ProductSmall> products=new ArrayList<>();

        if (TelegramUtil.isInt(parseString[0])){
            if (parseString.length>=2){
                products=helper.searchByCategory(user.getLang(),Integer.parseInt(parseString[0]),parseString[1]);
            }
            else {
                products=helper.searchByCategory(user.getLang(),Integer.parseInt(parseString[0]),"");
            }
        }else {
            products=helper.searchByName(user.getLang(),inlineQuery.getQuery());
        }

        for (ProductSmall product : products) {
            MessageTemplate messageTemplate=new MessageTemplate();
//            SendPhoto sendPhoto = messageTemplate.product(user, product.id);
//
//            MessageEntity messageEntity=new MessageEntity();
//            messageEntity.setLanguage(user.getLang().name());
//            messageEntity.setText("AAAA");


            InputTextMessageContent messageContent= InputTextMessageContent
                    .builder()
//                    .entities(Collections.singletonList(messageEntity))
                    .messageText(product.getImage().toString())
//                    .parseMode(ParseMode.MARKDOWN)
                    .build();
            InlineQueryResultArticle article= InlineQueryResultArticle
                    .builder()
                    .description(Translations.Price.get(user.getLang()) +" : "+product.price)
                    .title(product.getName())
                    .thumbUrl(product.getImage())
                    .id(String.valueOf(product.getId()))
                    .replyMarkup(new Col("a","a").getMarkup())
//                    .hideUrl(true)
                    .inputMessageContent(messageContent)
                    .build();
            results.add(article);
        }

        answer.setInlineQueryId(inlineQuery.getId());
        answer.setResults(results);
        System.out.println(answer.getCacheTime());
        return Collections.singletonList(answer);
    }




    @Override
    public State operatedBotState() {
        return State.SEARCH;
    }

    @Override
    public List<String> operatedCallBackQuery(TelegramUser user) {
        List<String> result=new ArrayList<>();
        result.add(State.SEARCH.name());
        return result;
    }


    protected String[] parseString(String str){
        String[] parts = str.split(",");
        System.out.println(Arrays.toString(parts));
        return parts;
    }




}
