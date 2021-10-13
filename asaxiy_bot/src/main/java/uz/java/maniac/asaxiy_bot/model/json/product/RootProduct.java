package uz.java.maniac.asaxiy_bot.model.json.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootProduct {
    public int status;
    public Product data;
}
