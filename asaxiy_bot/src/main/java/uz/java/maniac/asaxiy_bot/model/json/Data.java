package uz.java.maniac.asaxiy_bot.model.json;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    public List<Category> categories;
    public List<Brand> brands;
}
