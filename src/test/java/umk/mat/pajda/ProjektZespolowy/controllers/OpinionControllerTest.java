package umk.mat.pajda.ProjektZespolowy.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.OpinionService;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

@WebMvcTest(OpinionController.class)
@AutoConfigureMockMvc
@TestPropertySource(
    properties = {
      "FIXEDSALT_IPHASH = $2a$10$9elrbM0La5ooQgMP7i9yjO",
      "SHOP_ID = shop_id",
      "CLIENT_SECRET = client_secret",
      "CLIENT_ID = client_id",
      "profile = tests"
    })
public class OpinionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private OpinionService opinionService;

  @MockBean private RestTemplate restTemplate;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean private JWTService jwtService;

  @MockBean private AuthenticationServiceImpl authenticationService;

  @MockBean private ReviewService reviewService;

  @MockBean private BindingResult bindingResult;

  @Autowired private ObjectMapper objectMapper;

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
  public void shouldStatusOkWhenAddNewReviewTest() throws Exception {
    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setHashRevID("127.0.0.1");
    opinionPostDTO.setComment("fajne");
    opinionPostDTO.setRating(5);
    opinionPostDTO.setUserID(1);
    opinionPostDTO.setClientName("Adrian");
    opinionPostDTO.setAmount(500);
    opinionPostDTO.setCurrency("PLN");

    Mockito.when(reviewService.validateTime(any(OpinionPostDTO.class))).thenReturn(true);
    Mockito.when(opinionService.addOpinion(any(OpinionPostDTO.class), any(String.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));

    mockMvc
        .perform(
            post("/opinion")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opinionPostDTO)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "")
  public void shouldStatusNotAcceptableWhenAddNewReviewTest() throws Exception {
    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setHashRevID("127.0.0.1");
    opinionPostDTO.setComment("fajne");
    opinionPostDTO.setRating(5);
    opinionPostDTO.setUserID(1);
    opinionPostDTO.setClientName("Adrian");
    opinionPostDTO.setAmount(500);
    opinionPostDTO.setCurrency("PLN");

    Mockito.when(reviewService.validateTime(any(OpinionPostDTO.class))).thenReturn(true);
    Mockito.when(opinionService.addOpinion(any(OpinionPostDTO.class), any(String.class)))
        .thenReturn(null);

    mockMvc
        .perform(
            post("/opinion")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opinionPostDTO)))
        .andExpect(status().isNotAcceptable());
  }
}
