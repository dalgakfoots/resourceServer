package onthelive.kr.resourceServer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import onthelive.kr.resourceServer.entity.UserInfoEntity;
import onthelive.kr.resourceServer.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;

@Service
@Log4j2
@RequiredArgsConstructor
public class ResourceService {

    @Value("${authServer.introspectionEndpoint}")
    private String introspectionUrl;

    @Value("${authServer.getIdTokenEndPoint}")
    private String getIdTokenUrl;

    @Value("${resoServer.resourceId}")
    private String resourceId;
    @Value("${resoServer.resourceSecret}")
    private String resourceSecret;

    private final ResourceRepository resourceRepository;


    public boolean isIntrospectedAccessToken(HttpServletRequest request) {
        // ch-11-ex-4
        String accessToken = getInToken(request);
        ResponseEntity<HashMap> introspectionResponse = getResponseUsingAccessToken(accessToken, introspectionUrl);

        int statCode = introspectionResponse.getStatusCodeValue();
        if (statCode >= 200 && statCode < 300) {

            boolean active = (boolean) introspectionResponse.getBody().get("active");
            return active;
        }

        return false;
    }

    public ResponseEntity<HashMap<String, String>> getIdToken(HttpServletRequest request) {
        String accessToken = getInToken(request);
        ResponseEntity<HashMap<String, String>> idTokenResponse = getResponseUsingAccessToken(accessToken, getIdTokenUrl);
        return idTokenResponse;
    }

    public String encodeClientCredentials(String resourceId, String resourceSecret) {
        String temp = resourceId + ":" + resourceSecret;
        String encoded = new String(
                Base64.getEncoder().encode(temp.getBytes())
        );
        return encoded;
    }

    /*
    * private methods
    * */

    private String getInToken(HttpServletRequest request) {
        String auth = request.getHeader("authorization");
        String inToken = "";

        if (auth != null && auth.toLowerCase().contains("bearer")) {
            inToken = auth.split(" ")[1];
        } else if (request.getParameter("access_token") != null) {
            inToken = request.getParameter("access_token");
        }
        return inToken;
    }

    private ResponseEntity getResponseUsingAccessToken(String inToken, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + encodeClientCredentials(resourceId, resourceSecret));
        MultiValueMap<String , String> body = new LinkedMultiValueMap<>();
        body.add("token",inToken);

        HttpEntity entity = new HttpEntity(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<HashMap> response = restTemplate.postForEntity(url, entity, HashMap.class);
        return response;
    }

    public UserInfoEntity getUserInfoUsingSub(String sub) {
        return resourceRepository.getUserInfoUsingSub(sub);
    }
}
