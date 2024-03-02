package tn.esprit.easyfund.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static tn.esprit.easyfund.entities.Permission.ADMIN_CREATE;
import static tn.esprit.easyfund.entities.Permission.ADMIN_DELETE;
import static tn.esprit.easyfund.entities.Permission.ADMIN_READ;
import static tn.esprit.easyfund.entities.Permission.ADMIN_UPDATE;
import static tn.esprit.easyfund.entities.Permission.CLIENT_CREATE;
import static tn.esprit.easyfund.entities.Permission.CLIENT_DELETE;
import static tn.esprit.easyfund.entities.Permission.CLIENT_READ;
import static tn.esprit.easyfund.entities.Permission.CLIENT_UPDATE;
import static tn.esprit.easyfund.entities.Permission.INVESTOR_CREATE;
import static tn.esprit.easyfund.entities.Permission.INVESTOR_DELETE;
import static tn.esprit.easyfund.entities.Permission.INVESTOR_READ;
import static tn.esprit.easyfund.entities.Permission.INVESTOR_UPDATE;
import static tn.esprit.easyfund.entities.Permission.AGENT_CREATE;
import static tn.esprit.easyfund.entities.Permission.AGENT_DELETE;
import static tn.esprit.easyfund.entities.Permission.AGENT_READ;
import static tn.esprit.easyfund.entities.Permission.AGENT_UPDATE;

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
