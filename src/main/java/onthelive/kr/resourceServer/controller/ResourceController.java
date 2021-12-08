package onthelive.kr.resourceServer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import onthelive.kr.resourceServer.entity.LoginEntity;
import onthelive.kr.resourceServer.entity.UserInfoEntity;
import onthelive.kr.resourceServer.model.Login;
import onthelive.kr.resourceServer.model.UserInfo;
import onthelive.kr.resourceServer.service.AuthenticateService;
import onthelive.kr.resourceServer.service.ResourceService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ResourceController {

    private final ResourceService resourceService;
    private final AuthenticateService authenticateService;

    @PostMapping("/resource")
    public ResponseEntity postResource(HttpServletRequest request){
        boolean isIntrospected = resourceService.isIntrospectedAccessToken(request); // 토큰 인트로스펙션 실시
        if (isIntrospected == true) {

            // TODO 토큰 자체 검증 실시 해야함.

            HashMap response = new HashMap();
            response.put("name", "Protected Resource");
            response.put("description", "this data has been protected by dalgakfoots");
            return new ResponseEntity(response, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/userinfo")
    public ResponseEntity getUserInfo(HttpServletRequest request){
        boolean isIntrospected = resourceService.isIntrospectedAccessToken(request);
        // 토큰 인트로스펙션을 사용자 인증 여부와 동일하다고 전제한다.
        if (isIntrospected == true) {
            // Access Token 을 통해 Auth Server 에 저장된 Access Token 과 매칭되는 ID TOKEN 의 'sub' 를 확인한다.
            ResponseEntity<HashMap<String, String>> responseEntity = resourceService.getIdToken(request);
            String sub = responseEntity.getBody().get("sub");
            // sub 를 키로 사용하여 DB를 조회한다.
            UserInfoEntity userInfoEntity = resourceService.getUserInfoUsingSub(sub);
            // Entity를 DTO로 변환하여 리턴
            UserInfo userInfo = new UserInfo(
                    userInfoEntity.getSub(),
                    userInfoEntity.getName(),
                    userInfoEntity.getPreferredUserName(),
                    userInfoEntity.getEmail(),
                    userInfoEntity.isEmailVerified());

            return new ResponseEntity(userInfo , HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/userinfo")
    public void postUserInfo(){

    }

    @PostMapping("/authenticate")
    public ResponseEntity postAuthenticate(HttpServletRequest request) {
        String email = request.getParameter("email");
        String rawPassword = request.getParameter("password");

        // TODO 사용자 검증 절차 추가
        log.info("Authenticating ... email = "+ email + " / raw password = "+ rawPassword);
        LoginEntity requestLoginEntity = new LoginEntity(email, rawPassword);
        LoginEntity loginEntity = authenticateService.actionLogin(requestLoginEntity);

        if(loginEntity != null && !loginEntity.getEmail().equals("")){
            Login response = new Login(email , true);
            return new ResponseEntity(response, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
