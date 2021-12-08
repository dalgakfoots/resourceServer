package onthelive.kr.resourceServer.service;

import lombok.RequiredArgsConstructor;
import onthelive.kr.resourceServer.entity.LoginEntity;
import onthelive.kr.resourceServer.repository.AuthenticateRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final AuthenticateRepository authenticateRepository;

    public LoginEntity actionLogin(LoginEntity requestLoginEntity) {
        return authenticateRepository.actionLogin(requestLoginEntity);
    }

}
