package uz.java.maniac.asaxiy_bot.model.json.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.java.maniac.asaxiy_bot.model.json.Brand;
import uz.java.maniac.asaxiy_bot.model.json.ProductSmall;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    public int id;
    public String name;
    public Brand brand;
    public String category;
    public String model;
    public String description;
    public String main_image;
    public List<String> images;
    public Stat stat;
    public int state;
    public List<Tag> tag;
    public List<Object> review;
    public boolean hasDiscount;
    public int actual_price;
    public int old_price;
    public int partner_price;
    public List<ProductSmall> similar;
    public boolean is_installment;
    public int installment_starts;
}
