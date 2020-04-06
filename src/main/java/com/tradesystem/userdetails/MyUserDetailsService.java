package com.tradesystem.userdetails;


import com.tradesystem.user.User;
import com.tradesystem.user.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserDao userDao;

    public MyUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final User user = userDao.findByLogin(login)
                .orElseThrow(NoSuchElementException::new);
        return new MyUserDetails(user);
    }
/*
    public boolean login(LoginForm loginForm) {
        final UserDetails userDetails = loadUserByUsername(loginForm.getLogin());
        return passwordEncoder.matches(loginForm.getPassword(), userDetails.getPassword());
    }
*/
}
