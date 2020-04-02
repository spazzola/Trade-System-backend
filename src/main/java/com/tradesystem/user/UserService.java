package com.tradesystem.user;

import com.tradesystem.validation.EmailValidation;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

   private UserDao userDao;
   private EmailValidation emailValidation;

    public UserService(UserDao userDao, EmailValidation emailValidation) {
        this.userDao = userDao;
        this.emailValidation = emailValidation;
    }

    @Transactional
    public User registerUser(UserDto userDto) throws Exception {
        if (!emailValidation.validateEmail(userDto.getEmail())) {
            throw new Exception("Email error");
        }

        User user = User.builder()
                    .login(userDto.getLogin())
                    .password(userDto.getPassword())
                    .email(userDto.getEmail())
                    .build();

        return userDao.save(user);
    }

}
