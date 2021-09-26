package uz.java.maniac.asaxiy_bot.model.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    public int id;
    public String name;
    public String description;
    public String slug;
    public String image;
    public String mobile_image;
    public Category children;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", image='" + image + '\'' +
                ", mobile_image='" + mobile_image + '\'' +
                ", children=" + children +
                '}';
    }
}
