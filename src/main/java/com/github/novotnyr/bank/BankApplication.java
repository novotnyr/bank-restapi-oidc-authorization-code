package com.github.novotnyr.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@SpringBootApplication
@RestController
public class BankApplication {
    public static final Logger logger = LoggerFactory.getLogger(BankApplication.class);

    @GetMapping("/accounts/{accountId}/balance")
    public BigDecimal getBalance(@PathVariable String accountId, @AuthenticationPrincipal OidcUser oicUser) {
        String userId = oicUser.getPreferredUsername();
        logger.info("Retrieving bank account balance: account: {}, user {}", accountId, userId);
        return BigDecimal.TEN;
    }

    @Bean
    OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        return new RealmRoleUserService();
    }

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
