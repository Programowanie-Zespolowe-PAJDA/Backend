package umk.mat.pajda.ProjektZespolowy.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.Status;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.OpinionService;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.TipService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

@WebMvcTest(TipController.class)
@AutoConfigureMockMvc
public class TipControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean private OpinionService opinionService;

  @MockBean private RestTemplate restTemplate;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean private JWTService jwtService;

  @MockBean private AuthenticationServiceImpl authenticationService;

  @MockBean private ReviewService reviewService;
  @MockBean private TipService tipService;

  @TestConfiguration
  static class TestConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistrationBean(
        JwtAuthenticationFilter jwtFilter) {
      FilterRegistrationBean<JwtAuthenticationFilter> registrationBean =
          new FilterRegistrationBean<>(jwtFilter);
      registrationBean.setEnabled(false);
      return registrationBean;
    }
  }

  @Test
  @WithMockUser(roles = "")
  public void shouldStatusOkWhenAddTipTest() throws Exception {
    User user = new User();
    Tip tip = new Tip();
    tip.setUser(user);
    tip.setAmount(500);
    tip.setId("ID1");
    tip.setCurrency("PLN");
    tip.setCreatedAt(LocalDateTime.of(2017, Month.SEPTEMBER, 18, 18, 20));
    tip.setPaidWith("BLIK");
    String json =
        "{ \"order\": { \"orderId\": \"orderId\", \"status\": \"COMPLETED\", \"totalAmount\": \"500\", \"description\": \"PLN\", \"additionalDescription\": \"1\"} }";
    String header = "header";

    Review review = new Review();
    review.setStatus(Status.COMPLETED);

    Mockito.when(tipService.verifyNotification(json, header)).thenReturn(true);
    Mockito.when(tipService.getStatus(json)).thenReturn("COMPLETED");
    Mockito.when(tipService.getOrderId(json)).thenReturn("orderId");
    Mockito.when(tipService.getAdditionalDescription(json)).thenReturn("1");
    Mockito.when(tipService.getCurrency(json)).thenReturn("PLN");
    Mockito.when(tipService.getPaidWith("orderId")).thenReturn("BLIK");
    Mockito.when(tipService.getRealAmount()).thenReturn("553");
    Mockito.when(tipService.makePayout("orderId", "553")).thenReturn("payoutId");
    Mockito.when(tipService.addTip("payoutId", "orderId", "553", "BLIK", "PLN", "1"))
        .thenReturn(true);
    Mockito.when(reviewService.setStatus("orderId", Status.COMPLETED)).thenReturn(true);
    Mockito.when(reviewService.getReviewById("orderId")).thenReturn(review);

    mockMvc
        .perform(
            post("/tip")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("OpenPayu-Signature", "header"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "")
  public void shouldStatusUnauthorizedWhenAddTipTest() throws Exception {
    User user = new User();
    Tip tip = new Tip();
    tip.setUser(user);
    tip.setAmount(500);
    tip.setId("ID1");
    tip.setCurrency("PLN");
    tip.setCreatedAt(LocalDateTime.of(2017, Month.SEPTEMBER, 18, 18, 20));
    tip.setPaidWith("BLIK");
    String json =
        "{ \"order\": { \"orderId\": \"orderId\", \"status\": \"CANCELED\", \"totalAmount\": \"500\", \"description\": \"PLN\", \"additionalDescription\": \"1\"} }";
    String header = "headerBad";

    Mockito.when(tipService.verifyNotification(json, header)).thenReturn(false);

    mockMvc
        .perform(
            post("/tip")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("OpenPayu-Signature", "header"))
        .andExpect(status().isUnauthorized());
  }
}
