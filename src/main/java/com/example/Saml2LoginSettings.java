package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.saml2.Saml2LoginConfigurer;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.DefaultSaml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
class Saml2LoginSettings implements Customizer <Saml2LoginConfigurer<HttpSecurity>> {

    @Autowired
    GroupUtils groupUtils;


    @Autowired
    private IdentityProviderProperties identityProviderProperties;


    @Override
    public void customize(Saml2LoginConfigurer<HttpSecurity> t) {

        t.loginProcessingUrl("/saml/SSO");
        t.successHandler(new SavedRequestAwareAuthenticationSuccessHandler() {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                setTargets();
                //processAssertions (authentication, request);
                authentication = assignAuthorities (authentication, request);
                super.onAuthenticationSuccess(request, response, authentication);


            }

            private void setTargets () {
                setDefaultTargetUrl("/api/secure");
                setTargetUrlParameter("target");
                setAlwaysUseDefaultTargetUrl(true);
            }
        });


    }

   /* private void processAssertions (Authentication authentication, HttpServletRequest request) {
        DefaultSaml2AuthenticatedPrincipal princ = (DefaultSaml2AuthenticatedPrincipal) authentication.getPrincipal();
        request.getSession().setAttribute("givenName", princ.getFirstAttribute("urn:oid:0.9.2342.19200300.100.1.1"));
    }*/

    private Authentication assignAuthorities (Authentication authentication, HttpServletRequest request) {
        Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();

        List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
        updatedAuthorities.addAll(oldAuthorities);

        DefaultSaml2AuthenticatedPrincipal princ = (DefaultSaml2AuthenticatedPrincipal) authentication.getPrincipal();

        List<Object> appRoleNames =
                princ.getAttributes().keySet().stream()
                        .filter(key -> key.contains(identityProviderProperties.getRoleClaimAttribute()))
                        .map(princ.getAttributes()::get)
                        .collect(Collectors.toList()).get(0);


        for (Object appRoleName : appRoleNames) {
            updatedAuthorities.add(new SimpleGrantedAuthority("APPROLE_"+ String.valueOf(appRoleName)));

        }

        List<Object> groupIds = princ.getAttributes().keySet().stream()
                .filter(key -> key.contains(identityProviderProperties.getGroupClaimAttribute()))
                .map(princ.getAttributes()::get)
                .collect(Collectors.toList()).get(0);

        for (Object groupId : groupIds) {

            if(groupId!=null) {
                if (GroupUtils.GROUPID_GROUPNAME_CACHE.get(groupId) != null) {

                    updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + GroupUtils.GROUPID_GROUPNAME_CACHE.get(groupId)));
                } else {

                    updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + groupUtils.getGroupName(groupId)));
                }
            }

        }


        //updatedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

            Saml2Authentication sAuth = (Saml2Authentication) authentication;

            sAuth = new Saml2Authentication(
                    (AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                    sAuth.getSaml2Response(),
                    updatedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(sAuth);

            return sAuth;

    }
}