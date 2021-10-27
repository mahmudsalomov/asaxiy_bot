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
    ,
    Price(
            "\uD83D\uDCB8 Нархи: ",
            "\uD83D\uDCB8 Narxi: ",
            "\uD83D\uDCB8 Цена: "
    ),
    OverallPrice("Умумий нарх : ","Umumiy narx : ","Общая цена : "),
    DeleteBtn("❌ Ўчириш!","❌ O'chirish!","❌ Удалить!"),
    OrderBtn("✅Буюртма бериш!","✅Buyurtma berish!","✅Заказать!"),
    EmptyBasket("*❗ Саватча бўш!*","*❗ Savatcha bo'sh!*","*❗ Корзина пуста!*"),
    EmptyOrders("*Буюртмалар йўқ!*","*Buyurtmalar yo'q!*","*Нет заказов!*"),

    NeedPhoneMsg("*❗ Буюртмани тасдиқлаш учун телефон рақамингизни юборинг!*","*❗ Buyurtmani tasdiqlash uchun telefon raqamingizni yuboring!*","*❗ Отправьте свой номер телефона для подтверждения заказа!*"),
    NeedLocationMsg("*❗ Буюртмани тасдиқлаш учун манзилингизни ёзинг ёки геолокация юборинг!*",
            "*❗ Buyurtmani tasdiqlash uchun manzilingizni yozing yoki geolokatsiya yuboring!*",
            "*❗ Введите свой адрес или отправьте геолокацию для подтверждения заказа!*"),
    ConfirmOrderMsg("*Буюртмангиз учун раҳмат, лекин ҳозирча ботда буюртма бериш ишламайди\uD83D\uDE1C*",
            "*Buyurtmangiz uchun rahmat, lekin hozircha botda buyurtma berish ishlamaydi\uD83D\uDE1C*",
            "*Спасибо за заказ, но пока заказ на боте не работает\uD83D\uDE1C*"),
    SendPhoneBtn("\uD83D\uDCDEТелефон рақамни юбориш","\uD83D\uDCDETelefon raqamni yuborish","\uD83D\uDCDEОтправить номер телефона"),
    SendLocationBtn("\uD83D\uDCCDМанзил юбориш","\uD83D\uDCCDManzil yuborish","\uD83D\uDCCDОтправить адрес"),
    CancelBtn("❌Бекор қилиш","❌Bekor qilish","❌Отмена"),
    StopProcessMsg("\uD83D\uDED1Жараён тўхтатилди!","\uD83D\uDED1Jarayon to'xtatildi!","\uD83D\uDED1Процесс остановлен!"),

    MyOrdersBtn("\uD83D\uDD16Буюртмаларим","\uD83D\uDD16Buyurtmalarim","\uD83D\uDD16Мои заказы"),
    MyLanguageBtn("\uD83C\uDF0EТил","\uD83C\uDF0ETil","\uD83C\uDF0EЯзык"),
    Product("Маҳсулот : ","Mahsulot : ","Продукт : "),
    Categories("Категориялар","Kategoriyalar","Категории"),

    AddBasket("\uD83D\uDED2 Саватга жойлаш","\uD83D\uDED2 Savatga joylash","\uD83D\uDED2 Покупка");






//    private final Key key;
    private final String oz;
    private final String uz;
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
