package umk.mat.pajda.ProjektZespolowy.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;

@Service
public class JWTServiceImpl implements JWTService {

  private final Logger logger = LoggerFactory.getLogger(JWTServiceImpl.class);

  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSiginKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 604800000))
        .signWith(getSiginKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUserName(String token) throws SignatureException {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolvers)
      throws SignatureException {
    final Claims claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  @Profile("!tests")
  public Key getSiginKey() {
    byte[] key = Decoders.BASE64.decode(System.getenv("SECRET_KEY"));
    return Keys.hmacShaKeyFor(key);
  }

  public Claims extractAllClaims(String token) throws SignatureException {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(getSiginKey())
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      throw new SignatureException();
    }
  }

  public boolean isTokenValid(String token, UserDetails userDetails) throws SignatureException {
    final String username = extractUserName(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public boolean isTokenExpired(String token) throws SignatureException {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }
}
