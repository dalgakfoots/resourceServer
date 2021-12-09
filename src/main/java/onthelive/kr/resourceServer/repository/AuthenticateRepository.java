package onthelive.kr.resourceServer.repository;

import lombok.RequiredArgsConstructor;
import onthelive.kr.resourceServer.entity.LoginEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
public class AuthenticateRepository {

    private final EntityManager em;

    public LoginEntity actionLogin(LoginEntity requestLoginEntity){
        try {
            LoginEntity result = em.createQuery(
                            "select l from LoginEntity l where l.email =:email",
                            LoginEntity.class
                    )
                    .setParameter("email", requestLoginEntity.getEmail())
                    .getSingleResult();
            return result;
        } catch (NoResultException e){
            LoginEntity result = new LoginEntity("", "");
            return result;
        }
    }
}
