package uz.java.maniac.asaxiy_bot.model.message;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.ProfileEnum;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.Category;
import uz.java.maniac.asaxiy_bot.model.json.ProductSmall;
import uz.java.maniac.asaxiy_bot.model.json.Root;
import uz.java.maniac.asaxiy_bot.model.json.product.Product;
import uz.java.maniac.asaxiy_bot.model.order.OrderState;
import uz.java.maniac.asaxiy_bot.model.temp.RootModel;
import uz.java.maniac.asaxiy_bot.model.temp.TempRoot;
import uz.java.maniac.asaxiy_bot.repository.OrderRepository;
import uz.java.maniac.asaxiy_bot.repository.TelegramUserRepository;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;
import uz.java.maniac.asaxiy_bot.translations.Translations;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Col;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Row;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;
import uz.java.maniac.asaxiy_bot.utils.TestInterface;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createMessageTemplate;
//import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createPhotoTemplate;
import static uz.java.maniac.asaxiy_bot.translations.Translations.*;

@Component
public class MessageTemplate {
    @Autowired
    private TempRoot tempRoot;
    @Autowired
    UnirestHelper helper;
    @Autowired
    private TelegramUserRepository userRepository;


    public SendMessage removeProcess(TelegramUser user){
        SendMessage sendMessage = createMessageTemplate(user);
        ReplyKeyboardRemove replyKeyboardRemove=new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);
        sendMessage.setAllowSendingWithoutReply(true);
        sendMessage.setText(StopProcessMsg.get(user));
        user.setState(State.START);
        userRepository.save(user);
        return sendMessage;
    }

    public SendMessage orders(TelegramUser user){
        SendMessage sendMessage=createMessageTemplate(user);
        Col col=new Col();
        sendMessage.enableMarkdown(true);
        col.add(BackBtn.get(user), State.PROFILE.name());
        col.add(MainMenuBtn.get(user.getLang()),"EXIT");
        sendMessage.setText(EmptyOrders.get(user));
        sendMessage.setReplyMarkup(col.getMarkup());
        return sendMessage;
    }



    /** Edit messages **/
    public EditMessageText editText(TelegramUser user,String text,Integer messageId){
        EditMessageText editMessageText=new EditMessageText();
        editMessageText.setChatId(String.valueOf(user.getId()));
        editMessageText.setText(text);
        editMessageText.enableMarkdown(true);
        editMessageText.enableWebPagePreview();
        editMessageText.setMessageId(messageId);
        return editMessageText;
    }

    public EditMessageReplyMarkup editReplyMarkup(TelegramUser user,InlineKeyboardMarkup markup,Integer messageId){
        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(String.valueOf(user.getId()));
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(markup);
        return editMessageReplyMarkup;
    }

    public List<PartialBotApiMethod<? extends Serializable>> editTextAndReplyMarkup(TelegramUser user,Integer messageId,String text,InlineKeyboardMarkup markup){

        List<PartialBotApiMethod<? extends Serializable>> list=new ArrayList<>();
        list.add(editText(user, text, messageId));
        list.add(editReplyMarkup(user, markup, messageId));
        return list;

    }









    public SendMessage simple(TelegramUser user){
        SendMessage messageTemplate = createMessageTemplate(user);
        messageTemplate.setText("Salom");
        messageTemplate.enableMarkdown(true);
        return messageTemplate;

    }

    public SendMessage langChoice(TelegramUser user){
        try {
            Row row=new Row();
            row.add("\uD83C\uDDFA\uD83C\uDDFF Ўз", Lang.OZ.name());
            row.add("\uD83C\uDDFA\uD83C\uDDFF O'z", Lang.UZ.name());
            row.add("\uD83C\uDDF7\uD83C\uDDFA Ru", Lang.RU.name());
            SendMessage messageTemplate = createMessageTemplate(user);
            messageTemplate.setReplyMarkup(row.getMarkup());
            messageTemplate.setText("\uD83C\uDDFA\uD83C\uDDFFТилни танланг!\n\uD83C\uDDFA\uD83C\uDDFFTilni tanlang!\n\uD83C\uDDF7\uD83C\uDDFAВыберите язык!");
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
                col.add(BackBtn.get(user));
                col.add(MainMenuBtn.get(user),"EXIT");
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

    public SendMessage category(TelegramUser user,int id, int page,String back_callback){
        TestInterface<Category> util=new TestInterface<>();

        if (id<=0) return mainMenu(user);

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

                    boolean last=false;
                    boolean first=false;
                    if (util.totalPages(categories,6)<=page) last=true;
                    if (1>=page) first=true;
                    return categoryMessageBuilder(user,subList,id,page,util.totalPages(categories,6),first,last, Categories.get(user),back_callback);
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
                        boolean last=false;
                        boolean first=false;
                        if (util.totalPages(root.data.categories,6)<=page) last=true;
                        if (1>=page) first=true;
                        return categoryMessageBuilder(user,subList,id,page, util.totalPages(root.data.categories,6),first,last,Categories.get(user),back_callback);
                    }
                    return null;
                }

            }
            else{

                String name=rootModel.getParentName(id,rootModel);
                List<Category> categories = rootModel.getChildren(id, rootModel);

                if (categories==null){
                    List<ProductSmall> productSmallList = helper.getProductByCategory(user.getLang(), id);
                    return productByCategory(user,id,page,back_callback);
                }

                if (categories.size()>0){
                    List<Category> subList= util.pageable(categories,page,6);
                    boolean last=false;
                    boolean first=false;
                    if (util.totalPages(categories,6)<=page) last=true;
                    if (1>=page) first=true;
                    return categoryMessageBuilder(user,subList,id,page,util.totalPages(categories,6),first,last,name,back_callback);
                }

                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    protected SendMessage categoryMessageBuilder(TelegramUser user,List<Category> categories, int id, int page, int totalPages,boolean first, boolean last, String parentName, String back_callback){
        try {
            if (page<=0||page>totalPages) return null;
            Col col=new Col();
            categories.forEach(c->col.add(c.getName(),"c-"+c.getId()+"-1"));
            Row row=new Row();

            if (!first)
            row.add("⬅️️","c-"+id+"-"+(page-1));
            else row.add("⏹");


            InlineKeyboardButton search= InlineKeyboardButton
                    .builder()
                    .switchInlineQueryCurrentChat(id+",")
                    .text(Search.get(user))
                    .build();
            row.add(search);


            if (!last)
            row.add("➡️","c-"+id+"-"+(page+1));
            else row.add("⏹");
            col.add(row);

            col.add(BackBtn.get(user.getLang()),back_callback);
            col.add(MainMenuBtn.get(user.getLang()),"EXIT");
            return SendMessage
                    .builder()
                    .chatId(String.valueOf(user.getId()))
                    .text(!Objects.equals(parentName, "") ?parentName: Categories.get(user))
                    .replyMarkup(col.getMarkup())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public InlineKeyboardMarkup productKeyboard(Integer product_id, TelegramUser user){
        Col col=new Col();
        Row row=new Row();


        row.add("➖","p-"+product_id+"-minus");
        row.add("1","p-"+product_id);
        row.add("➕","p-"+product_id+"-plus");
        col.add(row);
        row.clear();
//                row.add("✅ Buyurtma berish","add_order");
        row.add(AddBasket.get(user),"addBasket-"+product_id+"-1");
        col.add(row);
//                    col.add("\uD83D\uDD19 Orqaga","backTo");
        col.add(BackBtn.get(user),"c-"+user.getCurrent_category_id()+"-1");
        col.add(MainMenuBtn.get(user),"EXIT");
        return col.getMarkup();
    }


    public InlineKeyboardMarkup productSearchKeyboard(ProductSmall product, TelegramUser user){
        Col col=new Col();
//        Row row=new Row();
//        row.add("➖","p-"+product.id+"-minus");
//        row.add("1","p-"+product.id);
//        row.add("➕","p-"+product.id+"-plus");
//        col.add(row);
//        row.clear();
//        row.add(AddBasket.get(user),"addBasket-"+product.id+"-1");
//        col.add(row);
        col.add(product.name,"p-"+product.id+"-1");
//        col.add(MainMenuBtn.get(user),"EXIT");
        return col.getMarkup();
    }

    public SendMessage productWithMessage(TelegramUser user, int id){
        try {
//            Col col=new Col();
//            Row row=new Row();
            Product product = helper.getProduct(user.getLang(), id);

//            Optional<ProductSmall> product = productRepository.findById(Long.valueOf(prodId));
            if (product!=null){

                System.out.println(product.getMain_image());
//                URL url=new URL(product.getMain_image());
//                URLConnection connection=url.openConnection();
                try {
                    String src="<a href='http://185.170.214.207:8081/view/"+product.id+"/"+user.getLang()+"'>Batafsil</a>";
//                    String src="<a href='https://www.youtube.com/watch?v=u_6eufrgceI'>aa</a>";
                    SendMessage photoTemplate = new SendMessage();
                    photoTemplate.setChatId(String.valueOf(user.getId()));
//                    InputFile file=new InputFile();
//                    file.setMedia(connection.getInputStream(),"Photo");
//                    photoTemplate.setPhoto(file);
                    photoTemplate.setParseMode(ParseMode.HTML);
//                    photoTemplate.setCaption(product.getName()+"\n"+Price.get(user)+product.getActual_price()+" \n");
//                    photoTemplate.setCaption(nameMessage(product.description));
                    photoTemplate.setDisableWebPagePreview(true);

                    photoTemplate.setText(product.getName()+"\n\n"+Price.get(user)+product.getActual_price()+" \n\n"+
                            "http://185.170.214.207:8081/view/"+product.id+"/"+user.getLang());
                    photoTemplate.setText("a\n"+src);

                    photoTemplate.setReplyMarkup(productKeyboard(id,user));




                    return photoTemplate;
//                    return createPhotoTemplate(user.getId()).setPhoto(
//                            "Photo",connection.getInputStream()
//                    ).setCaption(product.getName()+"\n\nNarxi: "+product.getActual_price()).setReplyMarkup(col.getMarkup());
//
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public SendPhoto product(TelegramUser user, int id){
        try {
//            Col col=new Col();
//            Row row=new Row();
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



//                row.add("➖","p-"+id+"-minus");
//                row.add("1","p-"+id);
//                row.add("➕","p-"+id+"-plus");
//                col.add(row);
//                row.clear();
////                row.add("✅ Buyurtma berish","add_order");
//                row.add("\uD83D\uDED2 Savatga joylash","addBasket-"+id+"-1");
//                col.add(row);
////                    col.add("\uD83D\uDD19 Orqaga","backTo");
//                col.add("\uD83D\uDD19 Orqaga","catId-"+user.getCurrent_category_id());
//                col.add("\uD83C\uDFD8 Bosh sahifa","EXIT");
                System.out.println(product.getMain_image());
                URL url=new URL(product.getMain_image());
                URLConnection connection=url.openConnection();
                try {
                    String src="<a href='http://localhost:8081/view/"+product.id+"/"+user.getLang()+"'>Batafsil</a>";
                    SendPhoto photoTemplate = new SendPhoto();
                    photoTemplate.setChatId(String.valueOf(user.getId()));
                    InputFile file=new InputFile();
                    file.setMedia(connection.getInputStream(),"Photo");
                    photoTemplate.setPhoto(file);
                    photoTemplate.setParseMode(ParseMode.HTML);
                    photoTemplate.setCaption(product.getName()+"\n"+Price.get(user)+product.getActual_price()+" \n\n"+src);
//                    photoTemplate.setCaption(nameMessage(product.description));


                    photoTemplate.setReplyMarkup(productKeyboard(id,user));




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

//    public String nameMessage(String str){
//        String name="";
//        name=str.replace("_", "\\_").replace("*", "\\*").replace("[", "\\[").replace("`", "\\`");
//        return name;
//    }


    public SendMessage productByCategory(TelegramUser user,int category_id, int page,String back_callback){
        try {
            TestInterface<ProductSmall> util=new TestInterface<>();
            List<ProductSmall> productSmallList = helper.getProductByCategory(user.getLang(), category_id);
            List<ProductSmall> subList= util.pageable(productSmallList,page,6);
            boolean last=false;
            boolean first=false;
            if (util.totalPages(productSmallList,6)<=page) last=true;
            if (1>=page) first=true;
            return productMessageBuilder(user,subList,page,util.totalPages(productSmallList,6),category_id,first,last,back_callback);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    protected SendMessage productMessageBuilder(TelegramUser user, List<ProductSmall> products, int page, int totalPages, int category_id,boolean first, boolean last,String back_callback){
        try {
            if (page<=0||page>totalPages) return null;
            Col col=new Col();
            products.forEach(p->col.add(p.getName(),"p-"+p.getId()+"-1"));
            Row row=new Row();


            if (!first)
                row.add("⬅️️","c-"+category_id+"-"+(page-1));
            else row.add("⏹");

            InlineKeyboardButton search = InlineKeyboardButton
                    .builder()
                    .switchInlineQueryCurrentChat(category_id+",")
                    .text(Search.get(user))
                    .build();
            row.add(search);

            if (!last)
                row.add("➡️","c-"+category_id+"-"+(page+1));
            else row.add("⏹");
            col.add(row);

//            col.add(BackBtn.get(user.getLang()),"c-"+category_id+"-1");
            col.add(BackBtn.get(user.getLang()),back_callback);
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

    public int findParent(List<Category> categories,int id){
        if (categories==null) return 0;
        for (Category category : categories) {
            if (category.children != null && category.children.size() > 0){
                for (Category child : category.children) {
                    if (child.id == id) return category.id;
                    int parent = findParent(category.children, id);
                    if (parent!=0) return parent;
                }
            }
        }
        return 0;

    }


    public String findParent(int id){
        boolean a=true;
        RootModel rootModel=tempRoot.get(Lang.OZ);
        List<Category> categories = rootModel.categories;

//        for (Category category : categories) {
            int parent = findParent(categories, id);
            if (parent!=0) return String.valueOf(parent);
//        }
        return String.valueOf(1);
    }


}
