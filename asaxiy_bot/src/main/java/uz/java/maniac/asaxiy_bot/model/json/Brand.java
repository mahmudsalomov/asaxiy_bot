package uz.java.maniac.asaxiy_bot.model.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    public int id;
    public String name;
    public int count;
    public String image;
    public boolean checked;
}
