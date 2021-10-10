package uz.java.maniac.asaxiy_bot.model.json;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRoot {
    public int status;
    public List<Product> data;
}
