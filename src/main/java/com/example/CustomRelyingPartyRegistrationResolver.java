package com.example;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;

public class CustomRelyingPartyRegistrationResolver implements RelyingPartyRegistrationResolver {

    private final RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

    public CustomRelyingPartyRegistrationResolver(RelyingPartyRegistrationRepository relyingPartyRegistrationRepository) {
        this.relyingPartyRegistrationRepository = relyingPartyRegistrationRepository;
    }


    public RelyingPartyRegistration resolve(HttpServletRequest request) {
        // Implement logic to resolve the appropriate RelyingPartyRegistration
        // based on the incoming request
        String registrationId = request.getParameter("registrationId");
        return relyingPartyRegistrationRepository.findByRegistrationId(registrationId);
    }

    @Override
    public RelyingPartyRegistration resolve(HttpServletRequest request, String relyingPartyRegistrationId) {
        return relyingPartyRegistrationRepository.findByRegistrationId(relyingPartyRegistrationId);
    }
}
