package umk.mat.pajda.ProjektZespolowy.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService {
  UserDetailsService userDetailsService();
}
