package umk.mat.pajda.ProjektZespolowy.misc;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduleTask {
  @Scheduled(fixedRate = 840000)
  public void callEndpoint() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getForObject("https://enapiwek-api.onrender.com/hello", String.class);
  }
}
