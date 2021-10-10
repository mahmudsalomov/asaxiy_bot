package uz.java.maniac.asaxiy_bot.utils;

import com.google.gson.Gson;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.callback.Callback;
import uz.java.maniac.asaxiy_bot.model.callback.Key;
import uz.java.maniac.asaxiy_bot.model.json.Category;
import uz.java.maniac.asaxiy_bot.model.json.ProductPostData;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().name("1-kat").id(1).build());
        categories.add(Category.builder().name("2-kat").id(2).build());
        categories.add(Category.builder().name("3-kat").id(3).build());
        categories.add(Category.builder().name("4-kat").id(4).build());
        categories.add(Category.builder().name("5-kat").id(5).build());
        categories.add(Category.builder().name("6-kat").id(6).build());
        categories.add(Category.builder().name("7-kat").id(7).build());
        categories.add(Category.builder().name("8-kat").id(8).build());
        categories.add(Category.builder().name("9-kat").id(9).build());
        categories.add(Category.builder().name("10-kat").id(10).build());

        TestInterface<Category> util=new TestInterface<>();
        System.out.println(util.totalPages(categories,6));
        System.out.println(util.totalPages(categories,11));
        System.out.println(util.totalPages(categories,4));
        System.out.println(util.totalPages(categories,5));
        util.pageable(categories,1,11).forEach(c-> System.out.println(c.toString()));


        System.out.println(util.parseString("p-2-3",3));

        Callback callback=new Callback();
        callback.setState(State.BASKET);
        callback.setKey(Key.BRAND_ID);

        Gson gson=new Gson();
        String json = gson.toJson(callback);
//        System.out.println(gson.fromJson(json,Callback.class));
        List<Integer> cs=new ArrayList<>();
        cs.add(96);
        cs.add(170);
        ProductPostData data= ProductPostData
                .builder()
                .categories(cs)
                .build();

        System.out.println(gson.toJson(data));
        UnirestHelper helper=new UnirestHelper();
        System.out.println(helper.getProductByCategory(Lang.UZ,96));







    }
}
