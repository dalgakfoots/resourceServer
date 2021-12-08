package onthelive.kr.resourceServer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "login_users")
public class LoginEntity {
    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    public LoginEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
