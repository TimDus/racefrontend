package racegame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService {


    //@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return null;
    }
}
