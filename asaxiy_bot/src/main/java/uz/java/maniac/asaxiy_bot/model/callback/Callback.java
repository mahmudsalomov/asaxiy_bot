package uz.java.maniac.asaxiy_bot.model.callback;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.java.maniac.asaxiy_bot.model.State;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Callback {
    private State state;
    private String key;
    private int page;
    private int id;

    public Callback(String key) {
        this.key = key;
    }

    public static Callback transform(String json){
        try {
            return new Gson().fromJson(json,Callback.class);
        }catch (Exception e){
            e.printStackTrace();
            return new Callback();
        }
    }

    @Override
    public String toString() {
        return "Callback{" +
                "state=" + state +
                ", key='" + key + '\'' +
                ", page=" + page +
                ", id=" + id +
                '}';
    }
}
