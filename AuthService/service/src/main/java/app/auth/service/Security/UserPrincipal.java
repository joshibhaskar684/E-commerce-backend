package app.auth.service.Security;

import app.auth.service.Entity.UserDetailsEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private UserDetailsEntity userDetailsEntity;

    public UserPrincipal(UserDetailsEntity userDetailsEntity) {
        this.userDetailsEntity = userDetailsEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+userDetailsEntity.getRole()));

    }

    @Override
    public String getPassword() {
        return userDetailsEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetailsEntity.getEmail();
    }
}
