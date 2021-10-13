package uz.java.maniac.asaxiy_bot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.InlineKeyboardModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TelegramUtil {
    public static SendMessage createMessageTemplate(TelegramUser user) {
        return createMessageTemplate(String.valueOf(user.getId()));
    }
//    public static EditMessageReplyMarkup createEditMessageTemplate(String chatId, String inlineID) {
//        return new EditMessageReplyMarkup().setChatId(chatId).setInlineMessageId(inlineID);
//    }

    public static SendMessage createMessageTemplate(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.enableMarkdown(true);
        return message;
    }

//    public static SendPhoto createPhotoTemplate(Long chatId){
//        return new SendPhoto().setChatId(String.valueOf(chatId));
//    }

//    public static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
//        return new InlineKeyboardButton()
//                .setText(text)
//                .setCallbackData(command);
//    }

    public static InlineKeyboardButton keyboard(String text){
        return InlineKeyboardModel
                .builder()
                .text(text)
                .callbackData("none")
                .build();
    }


    public static InlineKeyboardButton keyboard(String text, String callback){
        return InlineKeyboardModel
                .builder()
                .text(text)
                .callbackData(callback)
                .build();
    }

    public static InlineKeyboardButton keyboard(String text, String callback, String url){
        return InlineKeyboardModel
                .builder()
                .text(text)
                .callbackData(callback)
                .url(url)
                .build();
    }



    public static List<Object> pageable(List<Object> objectList, int page, int size){
        if (objectList == null||page<0||size<0) {
            return null;
        }

        List<Object> pageList = new ArrayList<>();
        int listSize=objectList.size();
        pageList=objectList.subList(page*size,page*size+size);
        return pageList;
    }


    public static String parseString(String str,int index){
        try {
            String[] parts = str.split("-");
            System.out.println(Arrays.toString(parts));
            if (index<0||parts.length<=index) return "";
            return parts[index];
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String[] parseString(String str){
            String[] parts = str.split("-");
            System.out.println(Arrays.toString(parts));
            return parts;
    }

    public static boolean isInt(String str){
        try {
            Integer.parseInt(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

//    public static String parseName(User user){
//        String str=" ";
//
//        if (user.getFirstname()==null&&user.getLastname()==null){
//            if (user.getUsername()!=null) str= user.getUsername();
//        } else {
//            if (user.getFirstname()!=null)
//                str+=user.getFirstname();
//            if (user.getLastname()!=null)
//                str+= user.getLastname();
//        }
//        return "*"+str+"*";
//    }
}
