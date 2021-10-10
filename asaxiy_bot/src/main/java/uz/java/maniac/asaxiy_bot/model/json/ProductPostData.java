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
public class ProductPostData {
    public List<Integer> categories;
    public List<Integer> brands;
    public int min_price;
    public int max_price;
    public int page;
    public String search_key;
    public int sort_by;
    public List<Object> options;
    public boolean partner;

}
