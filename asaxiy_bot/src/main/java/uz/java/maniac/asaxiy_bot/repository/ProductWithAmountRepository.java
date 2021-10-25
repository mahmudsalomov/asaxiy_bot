package uz.java.maniac.asaxiy_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.maniac.asaxiy_bot.model.order.Order;
import uz.java.maniac.asaxiy_bot.model.order.ProductOrder;
import uz.java.maniac.asaxiy_bot.model.order.ProductWithAmount;

import java.util.List;

public interface ProductWithAmountRepository extends JpaRepository<ProductWithAmount,Long> {
    List<ProductWithAmount> findAllByOrder(Order order);
    boolean existsByOrderAndProductOrder(Order order, ProductOrder product);
    ProductWithAmount findByOrderAndProductOrder(Order order, ProductOrder product);
}
