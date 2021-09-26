package uz.java.maniac.asaxiy_bot.service;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.json.Root;
import uz.java.maniac.asaxiy_bot.model.temp.RootModel;

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
            System.out.println(root);
            System.out.println(root.data);
            System.out.println(root.data.brands);
            System.out.println(root.data.categories);
            root.data.categories.forEach(c-> System.out.println(c.toString()));
            if (root.getStatus()==1)
//            rootModel.setRootCategory(root);
            return root;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }


}
