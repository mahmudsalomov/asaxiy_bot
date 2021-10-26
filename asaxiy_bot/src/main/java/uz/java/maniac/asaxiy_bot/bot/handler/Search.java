package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.ProductSmall;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;
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
    @Autowired
    private TelegramUserRepository telegramUserRepository;
    @Autowired
    private Product productHandler;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {
        System.out.println("MESSAGE = "+message);
        return null;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {
        return null;
    }


    // Istisno metod
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, Update update) throws IOException {

        try {
            List<List<InlineKeyboardButton>> keyboard = update.getMessage().getReplyMarkup().getKeyboard();
            CallbackQuery callbackQuery=new CallbackQuery();
            callbackQuery.setData(keyboard.get(0).get(0).getCallbackData());
            callbackQuery.setMessage(update.getMessage());
            System.out.println(keyboard.get(0).get(0).getCallbackData());
            return productHandler.handle(user,callbackQuery);
        }catch (Exception e){
            user.setState(State.START);
            telegramUserRepository.save(user);
            return null;
        }


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

        if (products==null||products.size()==0){
            InputTextMessageContent messageContent= InputTextMessageContent
                    .builder()
//                    .entities(Collections.singletonList(messageEntity))
                    .messageText("Hech narsa topilmadi")
//                    .parseMode(ParseMode.MARKDOWN)
                    .build();
            InlineQueryResultArticle article= InlineQueryResultArticle
                    .builder()
                    .description("")
                    .id("0")
                    .title("Hech narsa topilmadi!")
                    .inputMessageContent(messageContent)
                    .build();
            results.add(article);
        }
        else {
            for (ProductSmall product : products) {
                MessageTemplate messageTemplate=new MessageTemplate();
//            SendPhoto sendPhoto = messageTemplate.product(user, product.id);
//
//            MessageEntity messageEntity=new MessageEntity();
//            messageEntity.setLanguage(user.getLang().name());
//            messageEntity.setText("AAAA");

//                user.setState(State.START);

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
                        .replyMarkup(messageTemplate.productSearchKeyboard(product, user))
//                        .url(product.getImage())
//                    .hideUrl(true)
                        .inputMessageContent(messageContent)
                        .build();
                results.add(article);

            }
        }


        user.setState(State.SEARCH);
        telegramUserRepository.save(user);

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
