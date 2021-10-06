package uz.java.maniac.asaxiy_bot.model.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    public int id;
    public String name;
    public String description;
    public String slug;
    public String image;
    public String mobile_image;
    public List<Category> children;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", slug='" + slug + '\'' +
                ", image='" + image + '\'' +
                ", mobile_image='" + mobile_image + '\'' +
                ", children=" + children +
                '}';
    }
}
