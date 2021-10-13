package uz.java.maniac.asaxiy_bot.model.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.Category;
import uz.java.maniac.asaxiy_bot.model.json.ProductSmall;
import uz.java.maniac.asaxiy_bot.model.json.Root;
import uz.java.maniac.asaxiy_bot.model.json.product.Product;
import uz.java.maniac.asaxiy_bot.model.temp.RootModel;
import uz.java.maniac.asaxiy_bot.model.temp.TempRoot;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Col;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Row;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;
import uz.java.maniac.asaxiy_bot.utils.TestInterface;

import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createMessageTemplate;
//import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createPhotoTemplate;
import static uz.java.maniac.asaxiy_bot.translations.Translations.*;

@Component
public class MessageTemplate {
    @Autowired
    private TempRoot tempRoot;
    @Autowired
    UnirestHelper helper;


    public SendMessage simple(TelegramUser user){
        SendMessage messageTemplate = createMessageTemplate(user);
        messageTemplate.setText("Salom");
        messageTemplate.enableMarkdown(true);
        return messageTemplate;

    }

    public SendMessage langChoice(TelegramUser user){
        try {
            Row row=new Row();
            row.add("\uD83C\uDDFA\uD83C\uDDFF O'z", Lang.OZ.name());
            row.add("\uD83C\uDDFA\uD83C\uDDFF Ўз", Lang.UZ.name());
            row.add("\uD83C\uDDF7\uD83C\uDDFA Ru", Lang.RU.name());
            SendMessage messageTemplate = createMessageTemplate(user);
            messageTemplate.setReplyMarkup(row.getMarkup());
            messageTemplate.setText("Tilni tanlang!\nТилни танланг!\nВыберите язык!");
            messageTemplate.enableMarkdown(true);
            return messageTemplate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public SendMessage mainMenu(TelegramUser user){
        try {
            if (user.getLang()!=null){
                Col col =new Col();
                col.add(Products.get(user),State.PRODUCT.name());
                col.add(Promotions.get(user),State.PROMOTIONS.name());
                col.add(Basket.get(user),State.BASKET.name());
                col.add(Profile.get(user),State.PROFILE.name());
                InlineKeyboardButton search= InlineKeyboardButton
                        .builder()
                        .switchInlineQueryCurrentChat("")
                        .text(Search.get(user))
                        .build();
                col.add(search);
                return SendMessage
                        .builder()
                        .chatId(String.valueOf(user.getId()))
                        .text(String.format("" + Menu.get(user), user.getId()))
                        .replyMarkup(col.getMarkup())
                        .build();
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public SendMessage category(TelegramUser user,int id){
        RootModel rootModel=tempRoot.get(user.getLang());
        if (rootModel==null) return langChoice(user);
        try {
            if (id==1){



                Col col=new Col();
                rootModel.getCategories().forEach(c->col.add(c.getName(),"c"+c.getId()));

                InlineKeyboardButton search= InlineKeyboardButton
                        .builder()
                        .switchInlineQueryCurrentChat(id+",")
                        .text(Search.get(user))
                        .build();
                col.add(search);
                col.add("\uD83D\uDD19 Orqaga");
                col.add("\uD83C\uDFD8 Bosh sahifa","EXIT");
                return SendMessage
                        .builder()
                        .chatId(String.valueOf(user.getId()))
                        .text(String.format("test"))
                        .replyMarkup(col.getMarkup())
                        .build();
            }
            else{
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public SendMessage category(TelegramUser user,int id, int page){
        TestInterface<Category> util=new TestInterface<>();

        RootModel rootModel=tempRoot.get(user.getLang());
//        if (id==1){
//            if (rootModel.categories.size()>0){
//
//            }
//        }



        if (rootModel==null) return langChoice(user);
        try {
            if (id==1){
                if (rootModel.categories.size()>0){
                    Col col=new Col();
                    List<Category> categories=rootModel.categories;
                    List<Category> subList= util.pageable(categories,page,6);
                    return categoryMessageBuilder(user,subList,id,page,util.totalPages(categories,6));
//                    subList.forEach(c->col.add(c.getName(),"c-"+c.getId()+"-1"));
//
//                    Row row=new Row();
//                    row.add("<","c-"+id+"-"+(page-1));
//                    row.add(">","c-"+id+"-"+(page+1));
//                    col.add("\uD83D\uDD19 Orqaga");
//                    col.add("\uD83C\uDFD8 Bosh sahifa","EXIT");
//                    return SendMessage
//                            .builder()
//                            .chatId(String.valueOf(user.getId()))
//                            .text(String.format("test"))
//                            .replyMarkup(col.getMarkup())
//                            .build();
                }else {
                    Root root =helper.getRootCategory(user.getLang());
                    if (root.data.categories.size()>0){
                        List<Category> subList= util.pageable(root.data.categories,page,6);
                        return categoryMessageBuilder(user,subList,id,page, util.totalPages(root.data.categories,6));
                    }
                    return null;
                }

            }
            else{

                List<Category> categories = rootModel.getChildren(id, rootModel);

                if (categories==null){
                    List<ProductSmall> productSmallList = helper.getProductByCategory(user.getLang(), id);
                    return productByCategory(user,id,page);
                }

                if (categories.size()>0){
                    List<Category> subList= util.pageable(categories,page,6);
                    return categoryMessageBuilder(user,subList,id,page,util.totalPages(categories,6));
                }

                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    protected SendMessage categoryMessageBuilder(TelegramUser user,List<Category> categories, int id, int page, int totalPages){
        try {
            if (page<=0||page>totalPages) return null;
            Col col=new Col();
            categories.forEach(c->col.add(c.getName(),"c-"+c.getId()+"-1"));
            Row row=new Row();
            row.add("⬅️️","c-"+id+"-"+(page-1));


            InlineKeyboardButton search= InlineKeyboardButton
                    .builder()
                    .switchInlineQueryCurrentChat(id+",")
                    .text(Search.get(user))
                    .build();
            row.add(search);


            row.add("➡️","c-"+id+"-"+(page+1));
            col.add(row);

            col.add(BackBtn.get(user.getLang()));
            col.add(MainMenuBtn.get(user.getLang()),"EXIT");
            return SendMessage
                    .builder()
                    .chatId(String.valueOf(user.getId()))
                    .text("Kategoriyalar")
                    .replyMarkup(col.getMarkup())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public SendPhoto product(TelegramUser user, int id){
        try {
            Col col=new Col();
            Row row=new Row();
            Product product = helper.getProduct(user.getLang(), id);

//            Optional<ProductSmall> product = productRepository.findById(Long.valueOf(prodId));
            if (product!=null){

//                if (parseString3th(message).equals("minus")||parseString3th(message).equals("plus")){
//                    EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
//                    editMessageReplyMarkup.setChatId(String.valueOf(user.getChatId()));
//                    editMessageReplyMarkup.setMessageId(callback.getMessage().getMessageId());
//                    System.out.println(callback.getMessage().getReplyMarkup());
//                    InlineKeyboardMarkup markup=callback.getMessage().getReplyMarkup();
//                    String text = markup.getKeyboard().get(0).get(1).getText();
//                    String callbackData = markup.getKeyboard().get(1).get(0).getCallbackData();
//                    String [] s=callbackData.split("-");
//                    System.out.println(text);
//                    int quantity= Integer.parseInt(text);
//                    if (parseString3th(message).equals("minus")){
//                        if (quantity!=1){
//                            quantity-=1;
//                        }
//
//                    }else {
//                        quantity+=1;
//                    }
//                    markup.getKeyboard().get(0).get(1).setText(String.valueOf(quantity));
//                    markup.getKeyboard().get(1).get(0).setCallbackData(s[0]+"-"+s[1]+"-"+quantity);
//                    editMessageReplyMarkup.setReplyMarkup(markup);
//                    return Collections.singletonList(editMessageReplyMarkup);
//                }



                row.add("➖","p-"+id+"-minus");
                row.add("1","p-"+id);
                row.add("➕","p-"+id+"-plus");
                col.add(row);
                row.clear();
//                row.add("✅ Buyurtma berish","add_order");
                row.add("\uD83D\uDED2 Savatga joylash","addBasket-"+id+"-1");
                col.add(row);
//                    col.add("\uD83D\uDD19 Orqaga","backTo");
                col.add("\uD83D\uDD19 Orqaga","catId-"+user.getCurrent_category_id());
                col.add("\uD83C\uDFD8 Bosh sahifa","EXIT");
                System.out.println(product.getMain_image());
                URL url=new URL(product.getMain_image());
                URLConnection connection=url.openConnection();
                try {
                    SendPhoto photoTemplate = new SendPhoto();
                    photoTemplate.setChatId(String.valueOf(user.getId()));
                    InputFile file=new InputFile();
                    file.setMedia(connection.getInputStream(),"Photo");
                    photoTemplate.setPhoto(file);
                    photoTemplate.setCaption(product.getName()+"\n\nNarxi: "+product.getActual_price());
                    photoTemplate.setReplyMarkup(col.getMarkup());
                    return photoTemplate;
//                    return createPhotoTemplate(user.getId()).setPhoto(
//                            "Photo",connection.getInputStream()
//                    ).setCaption(product.getName()+"\n\nNarxi: "+product.getActual_price()).setReplyMarkup(col.getMarkup());
//
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }



    public SendMessage productByCategory(TelegramUser user,int category_id, int page){
        try {
            TestInterface<ProductSmall> util=new TestInterface<>();
            List<ProductSmall> productSmallList = helper.getProductByCategory(user.getLang(), category_id);
            List<ProductSmall> subList= util.pageable(productSmallList,page,6);
            return productMessageBuilder(user,subList,page,util.totalPages(productSmallList,6),category_id);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    protected SendMessage productMessageBuilder(TelegramUser user, List<ProductSmall> products, int page, int totalPages, int category_id){
        try {
            if (page<=0||page>totalPages) return null;
            Col col=new Col();
            products.forEach(p->col.add(p.getName(),"p-"+p.getId()+"-1"));
            Row row=new Row();
            row.add("⬅️️","c-"+category_id+"-"+(page-1));

            InlineKeyboardButton search = InlineKeyboardButton
                    .builder()
                    .switchInlineQueryCurrentChat(category_id+",")
                    .text(Search.get(user))
                    .build();
            row.add(search);

            row.add("➡️","c-"+category_id+"-"+(page+1));
            col.add(row);

            col.add(BackBtn.get(user.getLang()));
            col.add(MainMenuBtn.get(user.getLang()),"EXIT");
            return SendMessage
                    .builder()
                    .chatId(String.valueOf(user.getId()))
                    .text("Products")
                    .replyMarkup(col.getMarkup())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }




    public String parseString3th(String str){
        try {
            String[] parts = str.split("-");
            return parts[2];
        }catch (Exception e){
//            e.printStackTrace();
            return "";
        }
    }



}
