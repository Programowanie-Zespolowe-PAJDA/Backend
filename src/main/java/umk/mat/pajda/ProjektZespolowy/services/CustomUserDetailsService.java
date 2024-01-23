package umk.mat.pajda.ProjektZespolowy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.CustomUser;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepository.findByMail(mail);
        String role;
        if (user == null) {
            throw new UsernameNotFoundException(mail);
        }
        if(mail.equals("enapiwek@gmail.com")) {
            role = "ROLE_ADMIN";
        }
        else{
            role = "ROLE_AUTH";
        }
        return new CustomUser(user.getMail(), user.getPassword(), role);
    }
}
