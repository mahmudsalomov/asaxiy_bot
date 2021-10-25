package uz.java.maniac.asaxiy_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.java.maniac.asaxiy_bot.model.order.ProductOrder;

public interface ProductRepository extends JpaRepository<ProductOrder,Long> {
}
