package uz.java.maniac.asaxiy_bot.bot.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface Handler {
    List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException;
    List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException;

    State operatedBotState();

    List<String> operatedCallBackQuery(TelegramUser user);
}
