package uz.java.maniac.asaxiy_bot.model.order;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OrderBy
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;


    @ManyToOne
    private TelegramUser user;

    private String order_phone;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

}
