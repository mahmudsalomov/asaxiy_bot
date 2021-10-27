package uz.java.maniac.asaxiy_bot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.json.product.Product;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.model.temp.TempRoot;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;

@RestController
@RequestMapping("api")
public class Controller {

    @Autowired
    private UnirestHelper helper;
    @Autowired
    private TempRoot root;
    @Autowired
    private MessageTemplate messageTemplate;

    @GetMapping("test")
    public HttpEntity<?> test(){
        return ResponseEntity.ok(root.rootOZ.categories);
    }


    @GetMapping("test/{id}")
    public HttpEntity<?> test(@PathVariable Integer id){
        return ResponseEntity.ok(messageTemplate.findParent(id));
    }


}
