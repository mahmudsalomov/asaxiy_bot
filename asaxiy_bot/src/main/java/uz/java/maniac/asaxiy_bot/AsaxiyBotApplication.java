package uz.java.maniac.asaxiy_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AsaxiyBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsaxiyBotApplication.class, args);
    }

}
