package uz.java.maniac.asaxiy_bot.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.java.maniac.asaxiy_bot.model.temp.RootModel;
import uz.java.maniac.asaxiy_bot.model.temp.TempRoot;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;

@Component
public class SchulzTasks {
    @Autowired
    private TempRoot tempRoot;
    @Autowired
    private UnirestHelper helper;
    @Scheduled(fixedRate = 500000)
    public void transform(){
        tempRoot.setAllRoot();
    }




}
