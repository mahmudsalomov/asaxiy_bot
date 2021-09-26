package uz.java.maniac.asaxiy_bot.model.temp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import uz.java.maniac.asaxiy_bot.model.json.Brand;
import uz.java.maniac.asaxiy_bot.model.json.Category;
import uz.java.maniac.asaxiy_bot.model.json.Root;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootModel {
    private Root root;
    private List<Category> categories;
    private List<Brand> brands;

    public RootModel setRootCategory(Root root){
        this.root = root;
        this.categories = root.getData().getCategories();
        this.brands=root.getData().getBrands();

        System.out.println("Seeeeeeeet = "+categories.size());
        return this;
    }
}
