package com.tradesystem.user;


import com.tradesystem.jwt.AuthenticationRequest;
import com.tradesystem.jwt.AuthenticationResponse;
import com.tradesystem.jwt.JwtUtil;
import com.tradesystem.userdetails.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private MyUserDetailsService myUserDetailsService;
    private JwtUtil jwtUtil;

    public UserController(UserService userService, UserMapper userMapper,
                          AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public UserDto registerUser(@RequestBody UserDto userDto) throws Exception {
        User user = userService.registerUser(userDto);
        return userMapper.toDto(user);
    }

    @GetMapping("hello")
    public String hello() {

        return "Hello";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials ", e);
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getLogin());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
