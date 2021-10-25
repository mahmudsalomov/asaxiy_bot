package uz.java.maniac.asaxiy_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.order.Order;
import uz.java.maniac.asaxiy_bot.model.order.OrderState;
import uz.java.maniac.asaxiy_bot.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void removePhone(TelegramUser user){
        List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user, OrderState.DRAFT);
        for (Order order : orders) {
            order.setOrder_phone(null);
            orderRepository.save(order);
        }
    }
}
