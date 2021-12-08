package onthelive.kr.resourceServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Login {

    private String email;
    private boolean isAuthenticated;

}
