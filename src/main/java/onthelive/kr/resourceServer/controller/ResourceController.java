package onthelive.kr.resourceServer.controller;

import lombok.RequiredArgsConstructor;
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
    public void getUserInfo(){

    }

    @PostMapping("/userinfo")
    public void postUserInfo(){

    }

}
