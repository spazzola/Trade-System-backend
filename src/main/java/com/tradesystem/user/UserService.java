package com.tradesystem.user;

import com.tradesystem.validation.EmailValidation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

   private UserDao userDao;
   private EmailValidation emailValidation;
   private PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, EmailValidation emailValidation, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.emailValidation = emailValidation;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserDto userDto) throws Exception {
        if (!emailValidation.validateEmail(userDto.getEmail())) {
            throw new Exception("Email error");
        }
        final String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        final User user = User.builder()
                    .login(userDto.getLogin())
                    .password(encryptedPassword)
                    .email(userDto.getEmail())
                    .build();

        return userDao.save(user);
    }

}
