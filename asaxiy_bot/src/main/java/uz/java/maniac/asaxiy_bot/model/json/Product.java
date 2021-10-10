package uz.java.maniac.asaxiy_bot.model.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    public int id;
    public String name;
    public String image;
    public int price;
    public int old_price;
    public int partner_price;
    public Object brand;
    public boolean has_discount;
    public int rating;
    public int review_count;
}
