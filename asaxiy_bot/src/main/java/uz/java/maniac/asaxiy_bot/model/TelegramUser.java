package uz.java.maniac.asaxiy_bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Table(name = "telegram_user")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelegramUser {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;
    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Lang lang;
    @Enumerated(EnumType.STRING)
    private State state;



    @Column(name = "current_category_id", nullable = false)
    private Integer current_category_id;


    public TelegramUser(Long chatId) {
        this.id = chatId;
        this.state = State.START;
        this.current_category_id=1;
//        this.action="";
    }

    public TelegramUser(org.telegram.telegrambots.meta.api.objects.User user) {
        this.id = user.getId();
        this.username=user.getUserName();
        this.firstname=user.getFirstName();
        this.lastname=user.getLastName();
        this.state = State.START;
        this.current_category_id=1;
    }


}