package uz.java.maniac.asaxiy_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
}