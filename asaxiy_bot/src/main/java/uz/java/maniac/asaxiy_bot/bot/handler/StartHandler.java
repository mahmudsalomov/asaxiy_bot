package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Row;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.*;
import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createMessageTemplate;

@Component
public class StartHandler implements Handler{
    @Autowired
    private TelegramUserRepository telegramUserRepository;
    @Autowired
    private MessageTemplate messageTemplate;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {

        try {
            System.out.println(message);
            if (user.getLang()==null){

                return Collections.singletonList(messageTemplate.langChoice(user));

            }
            return Collections.singletonList(messageTemplate.mainMenu(user));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {

        String text=callback.getData();
        if (
                Objects.equals(text, Lang.RU.name())
                ||
                Objects.equals(text, Lang.UZ.name())
                        ||
                        Objects.equals(text, Lang.OZ.name()))
        {
            user.setLang(Lang.valueOf(text));
        }

//        SendMessage messageTemplate = createMessageTemplate(user);
//        messageTemplate.setText(user.getLang().name());

        return Collections.singletonList(messageTemplate.mainMenu(user));
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

    @Override
    public List<String> operatedCallBackQuery(TelegramUser user) {
        List<String> result = new ArrayList<>();
        result.add(Lang.OZ.name());
        result.add(Lang.UZ.name());
        result.add(Lang.RU.name());
        return result;
    }
}
