package umk.mat.pajda.ProjektZespolowy.services;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.ReportDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;


@Service
public class EmailService {
  private JavaMailSender javaMailSender;

  private final Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Async
  public void send(User user, String subject, String text) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(user.getMail());
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(text);
    javaMailSender.send(simpleMailMessage);
  }

  @Async
  public boolean sendReport(ReportDTO reportDTO) {
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setTo("enapiwek@gmail.com");
      simpleMailMessage.setSubject("Report from " + reportDTO.getNick());
      simpleMailMessage.setText(reportDTO.getText());
      javaMailSender.send(simpleMailMessage);
    }
    catch (Exception e){
      logger.error("send report", e);
      return false;
    }
    return true;
  }
}
