package am.ik.categolj2.domain.service.token;

import am.ik.categolj2.domain.model.User;
import am.ik.categolj2.domain.service.userdetails.Categolj2UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class Categolj2TokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() != null) {
            return accessToken;
        }
        Categolj2UserDetails userDetails = (Categolj2UserDetails) authentication.getPrincipal();
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> additionalInformation = new HashMap<>();
        User user = userDetails.getUser();
        user.setPassword(null);
        additionalInformation.put("user", user);
        defaultOAuth2AccessToken.setAdditionalInformation(additionalInformation);
        return defaultOAuth2AccessToken;
    }
}
