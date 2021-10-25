package uz.java.maniac.asaxiy_bot.model.order;

import lombok.*;
import uz.java.maniac.asaxiy_bot.model.json.Brand;
import uz.java.maniac.asaxiy_bot.model.json.ProductSmall;
import uz.java.maniac.asaxiy_bot.model.json.product.Stat;
import uz.java.maniac.asaxiy_bot.model.json.product.Tag;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long real_id;

    public String name;
    public String category;
    public String model;
    @Column(columnDefinition = "text")
    public String description;
    public String main_image;
    public int state;
    public boolean hasDiscount;
    public long actual_price;
    public long old_price;
    public long partner_price;
    public boolean is_installment;
    public int installment_starts;
}
