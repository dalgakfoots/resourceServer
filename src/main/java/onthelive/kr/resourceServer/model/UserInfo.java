package onthelive.kr.resourceServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String sub;

    private String name;

    private String preferred_username;

    private String email;

    private boolean email_verified;
}
