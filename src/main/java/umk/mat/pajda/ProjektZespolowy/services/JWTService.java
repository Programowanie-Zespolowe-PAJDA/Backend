package umk.mat.pajda.ProjektZespolowy.services;

import java.security.SignatureException;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
  String extractUserName(String token) throws SignatureException;

  String generateToken(UserDetails userDetails);

  boolean isTokenValid(String token, UserDetails userDetails) throws SignatureException;

  String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
