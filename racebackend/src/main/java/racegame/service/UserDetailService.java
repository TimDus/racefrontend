package racegame.service;

import racegame.model.User;
import racegame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailService implements UserDetailsService
{

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName)
    {
        User userToConvert = repository.findByUsername(userName);

        if (userToConvert != null) {
            return new org.springframework.security.core.userdetails.User(userToConvert.getUsername(), userToConvert.getPassword(), new ArrayList<>());
        }

        return null;
    }
}