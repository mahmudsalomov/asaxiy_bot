package uz.java.maniac.asaxiy_bot.model.order;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProductWithAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne
    private ProductOrder productOrder;

    @ManyToOne
    private Order order;

    private int amount;
}
