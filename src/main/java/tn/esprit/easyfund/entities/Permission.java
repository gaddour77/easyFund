package tn.esprit.easyfund.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    AGENT_READ("AGENT:read"),
    AGENT_UPDATE("AGENT:update"),
    AGENT_CREATE("AGENT:create"),
    AGENT_DELETE("AGENT:delete"),
    CLIENT_READ("CLIENT:read"),
    CLIENT_UPDATE("CLIENT:update"),
    CLIENT_CREATE("CLIENT:create"),
    CLIENT_DELETE("CLIENT:delete"),
    INVESTOR_READ("INVESTOR:read"),
    INVESTOR_UPDATE("INVESTOR:update"),
    INVESTOR_DELETE("INVESTOR:delete"),
    INVESTOR_CREATE("INVESTOR:create")

    ;

    @Getter
    private final String permission;
}
