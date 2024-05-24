package umk.mat.pajda.ProjektZespolowy.misc;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;
import umk.mat.pajda.ProjektZespolowy.services.EmailService;
import umk.mat.pajda.ProjektZespolowy.services.TokenService;

@Component
@Profile("prod")
public class ScheduleTask {

  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final EmailService emailService;

  public ScheduleTask(
      UserRepository userRepository, TokenService tokenService, EmailService emailService) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
    this.emailService = emailService;
  }

  @Scheduled(fixedRate = 840000)
  @Profile("prod")
  public void startTasks() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getForObject("https://enapiwek-api.onrender.com/hello", String.class);

    for (User user : userRepository.findByEnabled(false)) {
      if (tokenService.isExpired(user.getToken())) {
        userRepository.delete(user);
        emailService.send(
            user.getMail(),
            "Rejestracja anulowana",
            "Link do aktywacji konta wygasł, więc rejestracja została anulowana.");
      }
    }
  }
}
