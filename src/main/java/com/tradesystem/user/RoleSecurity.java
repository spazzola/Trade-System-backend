package com.tradesystem.user;

import com.tradesystem.userdetails.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.NoSuchElementException;

@Service
public class RoleSecurity {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    @Autowired
    private UserDao userDao;


    @Transactional
    public void checkAdminRole(Authentication authentication) {
        final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        final User user = userDao.findByLogin(myUserDetails.getUsername())
                .orElseThrow(NoSuchElementException::new);

        if(!ADMIN_ROLE.equals(user.getRole())) {
            throw new PermissionDeniedException();
        }
    }

    @Transactional
    public void checkUserRole(Authentication authentication) {
        final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        final User user = userDao.findByLogin(myUserDetails.getUsername())
                .orElseThrow(NoSuchElementException::new);

        if(!USER_ROLE.equals(user.getRole())) {
            throw new PermissionDeniedException();
        }
    }
}
