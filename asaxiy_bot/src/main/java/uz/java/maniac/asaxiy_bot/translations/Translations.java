package uz.java.maniac.asaxiy_bot.translations;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.java.maniac.asaxiy_bot.model.Lang;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;

@AllArgsConstructor
public enum Translations {

    Products("\uD83D\uDECD Маҳсулотлар","\uD83D\uDECD Mahsulotlar","\uD83D\uDECD Продукты"),
    Promotions("\uD83C\uDF89 Акциялар","\uD83C\uDF89 Aktsiyalar","\uD83C\uDF89 Акции"),
    Basket("\uD83D\uDED2 Саватча","\uD83D\uDED2 Savatcha","\uD83D\uDED2 Корзина"),
    Profile("\uD83D\uDC64 Профил","\uD83D\uDC64 Profil","\uD83D\uDC64 Профиль"),
    Search("\uD83D\uDD0D Қидирув","\uD83D\uDD0D Qidiruv","\uD83D\uDD0D Поиск"),
    BackBtn("\uD83D\uDD19 Орқага","\uD83D\uDD19 Orqaga","\uD83D\uDD19 Назад"),
    MainMenuBtn("\uD83C\uDFD8 Бош саҳифа","\uD83C\uDFD8 Bosh sahifa","\uD83C\uDFD8 Дом"),
    Menu(
            "Асахий ботига хуш келибсиз. Марҳамат хизмат турини танланг",
            "Asaxiy botiga xush kelibsiz. Marhamat xizmat turini tanlang!",
            "Добро пожаловать в asaxiy bot. Пожалуйста, выберите тип услуги")
    ;





//    private final Key key;
    private final String uz;
    private final String oz;
    private final String ru;

//    public Key getKey() {
//        return key;
//    }

    public String getUz() {
        return uz;
    }

    public String getOz() {
        return oz;
    }

    public String getRu() {
        return ru;
    }

  public String get(Lang lang){
        if (lang==Lang.OZ) return this.oz;
        if (lang==Lang.UZ) return this.uz;
        if (lang==Lang.RU) return this.ru;
        return "";

  }

    public String get(TelegramUser user){
        try {
            return get(user.getLang());
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }


    }


}
