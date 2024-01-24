package umk.mat.pajda.ProjektZespolowy.services;

import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
  String extractUserName(String token);

  String generateToken(UserDetails userDetails);

  boolean isTokenValid(String token, UserDetails userDetails);

  String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
