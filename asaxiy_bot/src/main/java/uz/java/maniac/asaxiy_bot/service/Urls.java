package uz.java.maniac.asaxiy_bot.service;

public interface Urls {

    String Prefix="https://api.asaxiy.uz/v1";
    String RootCategory=Prefix+"/product/root-category";
    String SubCategory=Prefix+"/product/sub-category";

    String products=Prefix+"/product/list";
    String product=Prefix+"/product/single?id=";
    String search=Prefix+"/product/search?key=";

}
