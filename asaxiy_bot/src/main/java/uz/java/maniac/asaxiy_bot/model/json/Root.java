package uz.java.maniac.asaxiy_bot.model.json;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Root extends Thread{
    public int status;
    public Data data;



}
