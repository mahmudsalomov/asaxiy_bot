package uz.java.maniac.asaxiy_bot.bot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import uz.java.maniac.asaxiy_bot.model.State;
import uz.java.maniac.asaxiy_bot.model.TelegramUser;
import uz.java.maniac.asaxiy_bot.model.json.product.Product;
import uz.java.maniac.asaxiy_bot.model.message.MessageTemplate;
import uz.java.maniac.asaxiy_bot.model.order.Order;
import uz.java.maniac.asaxiy_bot.model.order.OrderState;
import uz.java.maniac.asaxiy_bot.model.order.ProductOrder;
import uz.java.maniac.asaxiy_bot.model.order.ProductWithAmount;
import uz.java.maniac.asaxiy_bot.repository.OrderRepository;
import uz.java.maniac.asaxiy_bot.repository.ProductRepository;
import uz.java.maniac.asaxiy_bot.repository.ProductWithAmountRepository;
import uz.java.maniac.asaxiy_bot.service.UnirestHelper;
import uz.java.maniac.asaxiy_bot.translations.Translations;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Col;
import uz.java.maniac.asaxiy_bot.utils.ButtonModel.Row;
import uz.java.maniac.asaxiy_bot.utils.TelegramUtil;
import uz.java.maniac.asaxiy_bot.utils.TestInterface;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static uz.java.maniac.asaxiy_bot.utils.TelegramUtil.createMessageTemplate;

@Component
public class Basket implements Handler{

    @Autowired
    private MessageTemplate messageTemplate;
    @Autowired
    private UnirestHelper helper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductWithAmountRepository productWithAmountRepository;
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, String message) throws IOException {
        return null;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TelegramUser user, CallbackQuery callback) throws IOException {


        List<PartialBotApiMethod<? extends Serializable>> results=new ArrayList<>();


        List<Order> orders = orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.DRAFT);
        String[] parseString = TelegramUtil.parseString(callback.getData());






        if (parseString[0].equals("addBasket")){
            int id= Integer.parseInt(parseString[1]);
            int amount= Integer.parseInt(parseString[2]);
            Product product = helper.getProduct(user.getLang(), id);

            ProductOrder productOrder= ProductOrder.builder()
                    .actual_price(product.actual_price)
                    .real_id((long) product.id)
                    .category(product.category)
                    .description(product.description)
                    .main_image(product.main_image)
                    .installment_starts(product.installment_starts)
                    .hasDiscount(product.hasDiscount)
                    .is_installment(product.is_installment)
                    .old_price(product.old_price)
                    .name(product.name)
                    .model(product.model)
                    .partner_price(product.partner_price)
                    .state(product.state)
                    .build();
            productOrder=productRepository.save(productOrder);

            Order order;
            if (orders.size()>0){
                order=orders.get(0);
            }else {
                order= Order
                        .builder()
                        .orderState(OrderState.DRAFT)
                        .user(user)
                        .build();
                order=orderRepository.save(order);
            }

            ProductWithAmount productWithAmount=ProductWithAmount
                    .builder()
                    .amount(amount)
                    .productOrder(productOrder)
                    .order(order)
                    .build();
            productWithAmount=productWithAmountRepository.save(productWithAmount);


        }

        orders = orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.DRAFT);


        if (orders.size()==0){

            Col col=new Col();
            col.add(Translations.MainMenuBtn.get(user),"EXIT");
            SendMessage sendMessage=new SendMessage();
            SendMessage messageTemplate = createMessageTemplate(user);
            messageTemplate.setText(Translations.EmptyBasket.get(user));
            messageTemplate.setReplyMarkup(col.getMarkup());
            return Collections.singletonList(messageTemplate);
        }

        if (parseString[0].equals("amount")){
            productWithAmountRepository.deleteById(Long.valueOf(parseString[1]));
        }
        List<ProductWithAmount> amounts = productWithAmountRepository.findAllByOrder(orders.get(0));
        if (amounts.size()==0){
            orderRepository.deleteById(orders.get(0).getId());
            orders=orderRepository.findAllByUserAndOrderStateEquals(user,OrderState.DRAFT);
        }

        if (orders.size()==0){

            Col col=new Col();
            col.add(Translations.MainMenuBtn.get(user),"EXIT");
            SendMessage messageTemplate = createMessageTemplate(user);
            messageTemplate.setText(Translations.EmptyBasket.get(user));
            messageTemplate.setReplyMarkup(col.getMarkup());
            return Collections.singletonList(messageTemplate);
        }



        String text="";
        Col col=new Col();
        Row row=new Row();
        col.add(Translations.OrderBtn.get(user),"order-"+orders.get(0).getId());
        long fullAmount=0;
        for (ProductWithAmount amount:amounts){
            if (amount.getProductOrder().actual_price!=0){
                row.clear();
                text+="Mahsulot:\n*"+amount.getProductOrder().name+"\n"
                        +amount.getAmount()+"x"+amount.getProductOrder().actual_price+"="
                        +(amount.getAmount()*amount.getProductOrder().actual_price)+"*\n\n";
                row.add(Translations.DeleteBtn.get(user),"amount-"+amount.getId());
                row.add(amount.getProductOrder().name,"p-"+amount.getProductOrder().getReal_id()+"-1");
                col.add(row);
                fullAmount+=amount.getAmount()*amount.getProductOrder().actual_price;
            }
        }
        text+=Translations.OverallPrice.get(user)+" *"+fullAmount+"*";
        col.add(Translations.MainMenuBtn.get(user),"EXIT");

        SendMessage sendMessage = createMessageTemplate(user);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(col.getMarkup());


//        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
//        editMessageReplyMarkup.setChatId(String.valueOf(user.getId()));
//        editMessageReplyMarkup.setMessageId(callback.getMessage().getMessageId());
//        editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());
//
//
//        messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(),callback.getMessage().getMessageId());


        System.out.println("SAVATCHA");
        List<PartialBotApiMethod<? extends Serializable>> list = messageTemplate.editTextAndReplyMarkup(user, callback.getMessage().getMessageId(), sendMessage.getText(), col.getMarkup());
//        return Collections.singletonList(messageTemplate.editReplyMarkup(user, (InlineKeyboardMarkup) sendMessage.getReplyMarkup(),callback.getMessage().getMessageId()));
        return list;
    }

    @Override
    public State operatedBotState() {
        return State.BASKET;
    }

    @Override
    public List<String> operatedCallBackQuery(TelegramUser user) {
        List<String> result=new ArrayList<>();
        result.add("addBasket");
        result.add("amount");
//        result.add("order");
        result.add(State.BASKET.name());
        return result;
    }
}
