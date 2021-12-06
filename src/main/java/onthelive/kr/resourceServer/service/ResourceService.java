package onthelive.kr.resourceServer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

@Service
@Log4j2
public class ResourceService {

    @Value("${authServer.introspectionEndpoint}")
    private String introspectionUrl;

    @Value("${resoServer.resourceId}")
    private String resourceId;
    @Value("${resoServer.resourceSecret}")
    private String resourceSecret;

    public boolean isIntrospectedAccessToken(HttpServletRequest request) {
        // ch-11-ex-4
        String auth = request.getHeader("authorization");
        String inToken = "";

        if (auth != null && auth.toLowerCase().contains("bearer")) {
            inToken = auth.split(" ")[1];
        } else if (request.getParameter("access_token") != null) {
            inToken = request.getParameter("access_token");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + encodeClientCredentials(resourceId, resourceSecret));
        MultiValueMap<String , String> body = new LinkedMultiValueMap<>();
        body.add("token",inToken);

        HttpEntity entity = new HttpEntity(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<HashMap> response = restTemplate.postForEntity(introspectionUrl, entity, HashMap.class);

        int statCode = response.getStatusCodeValue();
        if (statCode >= 200 && statCode < 300) {

            boolean active = (boolean) response.getBody().get("active");
            return active;
        }

        return false;
    }

    public String encodeClientCredentials(String resourceId, String resourceSecret) {
        String temp = resourceId + ":" + resourceSecret;
        String encoded = new String(
                Base64.getEncoder().encode(temp.getBytes())
        );
        return encoded;
    }

}
