package uz.java.maniac.asaxiy_bot.model.json.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stat {
    public int vote;
    public int read_count;
    public int wish_count;
    public int share_count;
    public int review_count;
}
