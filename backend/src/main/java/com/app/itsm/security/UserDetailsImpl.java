package com.app.itsm.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.itsm.core.user.User;
import com.app.itsm.core.user.enums.UserRole;

public class UserDetailsImpl implements UserDetails {

    User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public static UserDetailsImpl create(User user) {
        return new UserDetailsImpl(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for(Integer i = user.getRole(); i >= 0; i--){
            authorities.add(new SimpleGrantedAuthority(UserRole.fromId(i).getName()));
        }
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getPasswordExpired();
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
        return user.getEnabled();
    }
    
}
