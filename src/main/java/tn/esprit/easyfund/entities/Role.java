package tn.esprit.easyfund.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static tn.esprit.easyfund.entities.Permission.*;

@RequiredArgsConstructor
public enum Role {


    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    AGENT_READ,
                    AGENT_UPDATE,
                    AGENT_DELETE,
                    AGENT_CREATE,
                    CLIENT_READ,
                    CLIENT_UPDATE,
                    CLIENT_DELETE,
                    CLIENT_CREATE,
                    INVESTOR_READ,
                    INVESTOR_UPDATE,
                    INVESTOR_DELETE,
                    INVESTOR_CREATE
            )
    ),
    AGENT(
            Set.of(
                    AGENT_READ,
                    AGENT_UPDATE,
                    AGENT_DELETE,
                    AGENT_CREATE
            )
    ),
    CLIENT(
            Set.of(
                    CLIENT_READ,
                    CLIENT_UPDATE,
                    CLIENT_DELETE,
                    CLIENT_CREATE
            )
    ),
    INVESTOR(
            Set.of(
                    INVESTOR_READ,
                    INVESTOR_UPDATE,
                    INVESTOR_DELETE,
                    INVESTOR_CREATE
            )
    )

    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
