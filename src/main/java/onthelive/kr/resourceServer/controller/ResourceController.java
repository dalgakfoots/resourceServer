package onthelive.kr.resourceServer.controller;

import lombok.RequiredArgsConstructor;
import onthelive.kr.resourceServer.entity.UserInfoEntity;
import onthelive.kr.resourceServer.model.UserInfo;
import onthelive.kr.resourceServer.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

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
        // => TODO Auth Server 내에 사용자 인증 절차 추가할 것!
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

}
