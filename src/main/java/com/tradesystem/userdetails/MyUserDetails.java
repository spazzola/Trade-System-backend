package com.tradesystem.userdetails;

import com.tradesystem.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private String userName;
    private String password;
    private String email;
    private List<GrantedAuthority> autorities;


    public MyUserDetails() {

    }

    public MyUserDetails(User user) {
        this.userName = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
       // this.autorities = Arrays.stream(user.getRoles().split(","))
                //.map(SimpleGrantedAuthority::new)
                //.collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return autorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
}
