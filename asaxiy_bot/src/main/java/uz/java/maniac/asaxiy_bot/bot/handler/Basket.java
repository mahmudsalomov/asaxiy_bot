package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
public class Basket implements Handler{
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {
        return null;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {
        return null;
    }

    @Override
    public State operatedBotState() {
        return State.BASKET;
    }

    @Override
    public List<String> operatedCallBackQuery(TelegramUser user) {
        return Collections.emptyList();
    }
}
