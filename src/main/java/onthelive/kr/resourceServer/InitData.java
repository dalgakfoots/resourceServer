package onthelive.kr.resourceServer;

import lombok.RequiredArgsConstructor;
import onthelive.kr.resourceServer.entity.LoginEntity;
import onthelive.kr.resourceServer.entity.UserInfoEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitData {

    private final InitDataForTest initData;

    @PostConstruct
    public void init(){
        initData.initUserInfo();
        initData.initLoginUser();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitDataForTest{

        private final EntityManager em;

        public void initUserInfo(){
            UserInfoEntity userInfo = new UserInfoEntity(
                    "9XE3-JI34-00132A",
                    "Alice",
                    "alice",
                    "alice.wonderland@example.com",
                    true
            );
            em.persist(userInfo);
        }

        public void initLoginUser(){
            LoginEntity loginEntity = new LoginEntity(
                    "alice.wonderland@example.com",
                    "a1s2d3"
            );

            em.persist(loginEntity);
        }

    }

}
