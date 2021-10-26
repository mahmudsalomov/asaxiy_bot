package uz.java.maniac.asaxiy_bot.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.json.product.Product;
import uz.java.maniac.asaxiy_bot.model.temp.TempRoot;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;

@Controller
@CrossOrigin
public class MvcController {
    @Autowired
    private UnirestHelper helper;
    @Autowired
    private TempRoot root;

    @GetMapping("/view/{product_id}/{lang}")
    public String view(@PathVariable Integer product_id, @PathVariable Lang lang, Model model){
        Product product = helper.getProduct(lang, product_id);
        model.addAttribute("product",product.description);
        model.addAttribute("photo",product.main_image);
        return "product";
    }
}
