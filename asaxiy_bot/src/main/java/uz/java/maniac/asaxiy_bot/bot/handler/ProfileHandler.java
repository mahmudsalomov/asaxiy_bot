package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import uz.java.maniac.asaxiy_bot.model.ProfileEnum;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;
import uz.java.maniac.asaxiy_bot.service.TelegramUserService;
import uz.java.maniac.asaxiy_bot.translations.Translations;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Col;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createMessageTemplate;

@Component
public class ProfileHandler implements Handler{
    @Autowired
    private TelegramUserService userService;

    @Autowired
    private MessageTemplate messageTemplate;

    @Autowired
    private StartHandler startHandler;

    @Autowired
    private TelegramUserRepository telegramUserRepository;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {
        return null;
    }

    public String nameMessage(TelegramUser user){
        String name="Profil : ";
        if (user.getFirstname()!=null){
            name+=user.getFirstname()+" ";
            if (user.getLastname()!=null){
                name+=user.getLastname();
            }
        }else {
            if (user.getUsername()!=null){
                name+=user.getUsername();
            }
        }
        name=name.replace("_", "\\_").replace("*", "\\*").replace("[", "\\[").replace("`", "\\`");
        return name;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {


        if (callback.getData().equals(State.PROFILE.name())){
            Col col = new Col();
            col.add(Translations.MyOrdersBtn.get(user),ProfileEnum.MY_ORDERS.name());
            col.add(Translations.MyLanguageBtn.get(user),ProfileEnum.MY_LANGUAGE.name());
            col.add(Translations.MainMenuBtn.get(user),"EXIT");
            SendMessage sendMessage = createMessageTemplate(user);
            sendMessage.setText(nameMessage(user));
            sendMessage.setReplyMarkup(col.getMarkup());
            return Collections.singletonList(sendMessage);
        }

        if (callback.getData().equals(ProfileEnum.MY_ORDERS.name())){
            return Collections.singletonList(messageTemplate.orders(user));
        }

        if (callback.getData().equals(ProfileEnum.MY_LANGUAGE.name())) {
            user.setLang(null);
            user=telegramUserRepository.save(user);
            return startHandler.handle(user,callback.getData());
        }
        return null;
    }

    @Override
    public State operatedBotState() {
        return State.PROFILE;
    }

    @Override
    public List<String> operatedCallBackQuery(TelegramUser user) {
        List<String> result=new ArrayList<>();
        result.add(State.PROFILE.name());
        result.add(ProfileEnum.MY_LANGUAGE.name());
        result.add(ProfileEnum.MY_ORDERS.name());
        return result;
    }
}
