package uz.java.maniac.asaxiy_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.order.Order;
import uz.java.maniac.asaxiy_bot.model.order.OrderState;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByOrderState(OrderState state);
    List<Order> findByOrderStateOrderByCreatedAtAsc(OrderState state);
    List<Order> findAllByUserAndOrderStateEquals(TelegramUser user, OrderState state);

}
