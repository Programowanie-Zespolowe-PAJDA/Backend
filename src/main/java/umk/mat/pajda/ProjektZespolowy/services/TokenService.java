package umk.mat.pajda.ProjektZespolowy.services;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.entity.Token;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.repository.TokenRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
@Profile("prod")
public class TokenService {

  private final TokenRepository tokenRepository;

  private final UserRepository userRepository;

  private final EmailService emailService;

  public TokenService(
      TokenRepository tokenRepository, UserRepository userRepository, EmailService emailService) {
    this.tokenRepository = tokenRepository;
    this.userRepository = userRepository;
    this.emailService = emailService;
  }

  public Token createToken(User user) {
    Token token = new Token();
    token.setToken(UUID.randomUUID().toString());
    token.setUser(user);
    token.setExpiryDate(LocalDateTime.now().plusHours(24));
    return token;
  }

  public Token updateToken(String email, User user) {
    Token token = null;
    try {
      token = tokenRepository.findByUser(user).get();
      token.setNewEmail(email);
      token.setToken(UUID.randomUUID().toString());
      token.setExpiryDate(LocalDateTime.now().plusHours(24));
    } catch (NoSuchElementException e) {
      return null;
    }
    return token;
  }

  public Token getToken(String token) {
    Token confirmToken = null;
    try {
      confirmToken = tokenRepository.findByToken(token).get();
    } catch (NoSuchElementException e) {
      return null;
    }
    return confirmToken;
  }

  public boolean isExpired(Token token) {
    LocalDateTime expiryDate = token.getExpiryDate();
    LocalDateTime currentDate = LocalDateTime.now();
    if (expiryDate.isBefore(currentDate)) {
      return true;
    }
    return false;
  }

  public boolean confirm(Token token) {
    User user = null;
    try {
      user = userRepository.findByToken(token).get();
      if (user.isEnabled()) {
        return false;
      }
      user.setEnabled(true);
      userRepository.save(user);
    } catch (NoSuchElementException e) {
      return false;
    }
    return true;
  }
}
