package racegame.controller;

import racegame.service.UserService;
import racegame.model.User;
import racegame.response.AuthenticationRequest;
import racegame.response.LoginResponse;
import racegame.response.RegisterResponse;
import racegame.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/account")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping(value = "/register")
    public ResponseEntity<RegisterResponse> addUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            RegisterResponse registerResponse = new RegisterResponse();
            registerResponse.setSuccess(true);

            return ResponseEntity.ok(registerResponse);
        }  catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/authenticate")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            userService.login(authenticationRequest);
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) userService.loadUserByUsername(authenticationRequest.getUsername());

            final String jwt = jwtTokenUtil.generateToken(user);

            LoginResponse loginResponse = new LoginResponse(jwt);
            loginResponse.setSuccess(true);

            return ResponseEntity.ok(loginResponse);
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Incorrect credentials"
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Something went wrong"
            );
        }
    }
}
