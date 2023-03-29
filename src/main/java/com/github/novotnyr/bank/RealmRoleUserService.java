package com.github.novotnyr.bank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.HashSet;
import java.util.Set;

public class RealmRoleUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    public static final String REALM_ROLES_CLAIM = "realm_roles";

    private String realmRolesClaim = REALM_ROLES_CLAIM;

    private final OidcUserService delegateUserService = new OidcUserService();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = this.delegateUserService.loadUser(userRequest);

        Set<GrantedAuthority> allAuthorities = new HashSet<>(oidcUser.getAuthorities());
        oidcUser.getClaimAsStringList(this.realmRolesClaim)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .forEach(allAuthorities::add);
        return new DefaultOidcUser(allAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
