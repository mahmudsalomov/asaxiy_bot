package uz.java.maniac.asaxiy_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.temp.RootModel;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;

@Service
public class TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

//    @Autowired
//    private RootModel rootModel;

    public TelegramUserService(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    public void lastCallback(TelegramUser user, CallbackQuery callback){
        try {
            user.setLastCallback(callback.getData());
            telegramUserRepository.save(user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public CallbackQuery lastCallback(){
//        CallbackQuery callback=new CallbackQuery();
//        callback.se
//    }

//    @Scheduled(fixedRate = 1000)
//    public void test(){
//        System.out.println(rootModel.getCategories().size()+" "+new Date());
//    }

//    public
}
