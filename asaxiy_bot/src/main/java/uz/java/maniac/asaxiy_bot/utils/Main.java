package uz.java.maniac.asaxiy_bot.utils;

import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.Category;

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
//        System.out.println(Math.ceil(1.2));
////        System.out.println(Math.ceil(1.6));
//        util.pageable(categories,1,12).forEach(System.out::println);
//        System.out.println(util.pageable(categories,1,3));




    }
}
