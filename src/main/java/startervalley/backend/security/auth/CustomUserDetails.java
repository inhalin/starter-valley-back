package startervalley.backend.security.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import startervalley.backend.entity.Role;
import startervalley.backend.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final User user;
    private Map<String, Object> attributes;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(()-> {
            if (user != null) {
                return user.getRole().name();
            }
            return Role.ANONYMOUS.name();
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("email");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        if (user != null) {
            return user.getUsername();
        }
        return "newUser";
    }
}

