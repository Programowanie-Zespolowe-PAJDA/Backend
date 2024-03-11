package umk.mat.pajda.ProjektZespolowy.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;
import umk.mat.pajda.ProjektZespolowy.services.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  public AuthServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return userRepository
            .findByMail(mail)
            .orElseThrow(() -> new UsernameNotFoundException("User Not found"));
      }
    };
  }
}
