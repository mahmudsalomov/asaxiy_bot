package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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
            return Collections.singletonList(messageTemplate.category(user,Integer.parseInt(parseString[1]),Integer.parseInt(parseString[2])));
        }

//        if (parseString[0].equals("p")){
//            return Collections.singletonList(messageTemplate.productByCategory(user,Integer.parseInt(parseString[1]),Integer.parseInt(parseString[2])));
//        }

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
        return result;
    }
}
