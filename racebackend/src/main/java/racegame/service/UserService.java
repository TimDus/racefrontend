package racegame.service;

import racegame.model.User;
import racegame.repository.UserRepository;
import racegame.response.AuthenticationRequest;
import racegame.util.PasswordHasher;
import racegame.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User getUser(int id) {
        return repository.findById(id).orElseThrow(() -> null);
    }

    public User getUserByName(String name){ return repository.findByUsername(name); }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public User updateUser(User user) {
        return repository.save(user);
    }

    public void deleteUser(int id){
        repository.deleteById(id);
    }

    public void addUser(User user) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (repository.findByUsername(user.getUsername()) != null) {
            return;
        }

        String hashedPassword = PasswordHasher.generateStrongPasswordHash(user.getPassword());
        user.setPassword(hashedPassword);
        repository.save(user);
    }

    public User login(AuthenticationRequest authenticationRequest) throws InvalidKeySpecException, NoSuchAlgorithmException, AccessDeniedException {
        User user = new User();
        User userToCheck = repository.findByUsername(authenticationRequest.getUsername());
        if (userToCheck != null) {
            if (PasswordValidator.validatePassword(authenticationRequest.getPassword(), userToCheck.getPassword())) {
                user.setUsername(authenticationRequest.getUsername());
                user.setPassword(authenticationRequest.getPassword());
                return user;
            }
        }

        throw new AccessDeniedException("Access Denied!");
    }

    //@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userToConvert = repository.findByUsername((username));
        if (userToConvert != null) {
            return new org.springframework.security.core.userdetails.User(
                    userToConvert
                        .getUsername(), userToConvert
                        .getPassword(), new ArrayList<>());
        }

        return null;
    }
}
