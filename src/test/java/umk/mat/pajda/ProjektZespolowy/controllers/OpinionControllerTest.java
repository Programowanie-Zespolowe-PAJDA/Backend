package umk.mat.pajda.ProjektZespolowy.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.OpinionService;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.TipService;
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
  @MockBean private TipService tipService;
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
    User user = new User();

    Mockito.when(reviewService.getUser(1)).thenReturn(user);
    Mockito.when(reviewService.validateTime(user, opinionPostDTO.getHashRevID())).thenReturn(true);
    Mockito.when(
            opinionService.addOpinion(any(OpinionPostDTO.class), eq("127.0.0.1"), eq(500), eq("1")))
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

    Mockito.when(reviewService.validateTime(any(User.class), any(String.class))).thenReturn(true);
    Mockito.when(
            opinionService.addOpinion(any(OpinionPostDTO.class), eq("127.0.0.1"), eq(500), eq("1")))
        .thenReturn(null);

    mockMvc
        .perform(
            post("/opinion")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opinionPostDTO)))
        .andExpect(status().isNotAcceptable());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void shouldStatusOkWhenGetOpinionsTest() throws Exception {
    List<OpinionGetDTO> list = List.of(new OpinionGetDTO(5, 500, "USD", "komentarz", "klient"));

    Mockito.when(opinionService.getOpinions(any(String.class))).thenReturn(list);
    MvcResult mvcResult =
        mockMvc
            .perform(
                get("/opinion")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    Assertions.assertEquals(
        "[{\"rating\":5,\"amount\":500,\"currency\":\"USD\",\"comment\":\"komentarz\",\"clientName\":\"klient\"}]",
        mvcResult.getResponse().getContentAsString());
  }
}
