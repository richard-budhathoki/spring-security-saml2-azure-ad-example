package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityProviderProperties {
    @Value("${identity-provider.client-id}")
    private String clientId;
    @Value("${identity-provider.client-secret}")
    private String clientSecret;
    @Value("${identity-provider.tenant-id}")
    private String tenantId;
    @Value("${identity-provider.scope}")
    private String scope;
    @Value("${identity-provider.group-claim-attribute}")
    private String groupClaimAttribute;

    public String getRoleClaimAttribute() {
        return roleClaimAttribute;
    }

    public void setRoleClaimAttribute(String roleClaimAttribute) {
        this.roleClaimAttribute = roleClaimAttribute;
    }

    @Value("${identity-provider.role-claim-attribute}")
    private String roleClaimAttribute;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGroupClaimAttribute() {
        return groupClaimAttribute;
    }

    public void setGroupClaimAttribute(String groupClaimAttribute) {
        this.groupClaimAttribute = groupClaimAttribute;
    }
}
