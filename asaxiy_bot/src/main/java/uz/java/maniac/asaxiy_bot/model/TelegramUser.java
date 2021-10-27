package uz.java.maniac.asaxiy_bot.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Objects;


@Table(name = "telegram_user")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
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

    @Column(columnDefinition = "text")
    private String location;

    private String lastCallback;



    @Column(name = "current_category_id", nullable = false)
    private Integer current_category_id;


    public TelegramUser(Long chatId) {
        this.id = chatId;
        this.state = State.START;
        this.current_category_id=1;
    }

    public TelegramUser(org.telegram.telegrambots.meta.api.objects.User user) {
        this.id = user.getId();
        this.username=user.getUserName();
        this.firstname=user.getFirstName();
        this.lastname=user.getLastName();
        this.state = State.START;
        this.current_category_id=1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TelegramUser that = (TelegramUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}