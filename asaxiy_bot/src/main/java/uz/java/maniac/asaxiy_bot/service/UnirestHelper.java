package uz.java.maniac.asaxiy_bot.service;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.*;
import uz.java.maniac.asaxiy_bot.model.temp.RootModel;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnirestHelper {
//    @Autowired
//    private RootModel rootModel;

    public Root getRootCategory(Lang lang){
        try{
            HttpResponse<JsonNode> response = Unirest.get(Urls.RootCategory).header("language",lang.name().toLowerCase()).asJson();
            Gson gson = new Gson();
            String responseJSONString = response.getBody().toString();
            Root root = gson.fromJson(responseJSONString, Root.class);
//            System.out.println(root);
//            System.out.println(root.data);
//            System.out.println(root.data.brands);
//            System.out.println(root.data.categories);
//            root.data.categories.forEach(c-> c.setChildren(getSubCategory(lang,c.id).data.categories));
//            for (Category c : root.data.categories) {
//
//            }
//            root.data.categories.forEach(c-> System.out.println(c.getId()+" = "+c.children.size()));

            System.out.println(root.data.categories.get(0).name);

            if (root.getStatus()==1)
//            rootModel.setRootCategory(root);
            return root;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public SubRoot getSubCategory(Lang lang, int id){
        try {
            HttpResponse<JsonNode> response = Unirest.get(Urls.SubCategory+"?id="+id+"&partner=true").header("language",lang.name().toLowerCase()).asJson();
            Gson gson = new Gson();
            String responseJSONString = response.getBody().toString();
            SubRoot subRoot = gson.fromJson(responseJSONString, SubRoot.class);
            if(subRoot.getData().size()==0) return null;
//            root.data.categories.forEach(c-> c.setChildren(getSubCategory(lang,c.id).data.categories));
//            for (int i = 0; i < root.data.categories.size(); i++) {
//
//                System.out.println(c.getId()+" sub = "+c.children.size())
//            }
//            root.data.categories.forEach(c-> System.out.println(c.getId()+" sub = "+c.children.size()));
            return subRoot;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public List<Product> getProductByCategory(Lang lang,int category_id){
        List<Integer> cs=new ArrayList<>();
        cs.add(category_id);
        ProductPostData data= ProductPostData
                .builder()
                .categories(cs)
                .sort_by(-1)
                .build();
        HttpResponse<JsonNode> response = Unirest.post(Urls.product).body(data).header("language",lang.name().toLowerCase()).asJson();
        Gson gson = new Gson();
        String responseJSONString = response.getBody().toString();
        ProductRoot root = gson.fromJson(responseJSONString, ProductRoot.class);
        return root.data;
    }


}
