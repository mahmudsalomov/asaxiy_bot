package uz.java.maniac.asaxiy_bot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.java.maniac.asaxiy_bot.model.temp.TempRoot;

@RestController
@RequestMapping("api")
public class Controller {

    @Autowired
    private TempRoot root;

    @GetMapping("test")
    public HttpEntity<?> test(){
        return ResponseEntity.ok(root.rootOZ.categories);
    }
}
