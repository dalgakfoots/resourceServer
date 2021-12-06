package onthelive.kr.resourceServer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "userinfos")
public class UserInfoEntity {
    @Id
    private String sub;

    private String name;

    @Column(name="preferred_username")
    private String preferredUserName;

    private String email;

    @Column(name="email_verified")
    private boolean emailVerified;
}
