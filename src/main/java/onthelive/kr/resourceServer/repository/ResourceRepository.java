package onthelive.kr.resourceServer.repository;

import lombok.RequiredArgsConstructor;
import onthelive.kr.resourceServer.entity.UserInfoEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ResourceRepository {

    private final EntityManager em;


    public UserInfoEntity getUserInfoUsingSub(String sub) {
        return em.find(UserInfoEntity.class, sub);
    }
}
