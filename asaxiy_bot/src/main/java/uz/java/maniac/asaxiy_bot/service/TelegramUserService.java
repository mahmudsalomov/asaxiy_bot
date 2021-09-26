package uz.java.maniac.asaxiy_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

//    @Scheduled(fixedRate = 1000)
//    public void test(){
//        System.out.println(rootModel.getCategories().size()+" "+new Date());
//    }

//    public
}
