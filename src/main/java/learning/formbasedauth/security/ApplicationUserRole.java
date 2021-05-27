package learning.formbasedauth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static learning.formbasedauth.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    NORMAL(new HashSet<ApplicationUserPermission>(Arrays.asList(READ))),
    ADMIN(new HashSet<ApplicationUserPermission>(Arrays.asList(READ, WRITE)));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = getPermissions().stream().
                map(permission -> new SimpleGrantedAuthority(permission.getPermission())).
                collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
