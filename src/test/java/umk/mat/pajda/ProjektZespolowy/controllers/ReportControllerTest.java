package umk.mat.pajda.ProjektZespolowy.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.ReportDTO;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.services.EmailService;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

@WebMvcTest(ReportController.class)
@AutoConfigureMockMvc
@TestPropertySource(
    properties = {
      "FIXEDSALT_IPHASH = $2a$10$9elrbM0La5ooQgMP7i9yjO",
      "SHOP_ID = shop_id",
      "CLIENT_SECRET = client_secret",
      "CLIENT_ID = client_id",
      "profile = tests"
    })
public class ReportControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private EmailService emailService;

  @MockBean private RestTemplate restTemplate;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean private JWTService jwtService;

  @MockBean private AuthenticationServiceImpl authenticationService;

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
  public void shouldStatusOkTest() throws Exception {
    ReportDTO reportDTO = new ReportDTO();
    reportDTO.setNick("Anonim");
    reportDTO.setText("text");

    Mockito.when(emailService.sendReport(any(ReportDTO.class))).thenReturn(true);

    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/reports")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reportDTO)))
            .andExpect(status().isOk())
            .andReturn();
    Assertions.assertEquals("success", mvcResult.getResponse().getContentAsString());
  }

  @Test
  @WithMockUser(roles = "")
  public void shouldStatusNotAcceptableWhenValidationErrorsTest() throws Exception {
    ReportDTO reportDTO = new ReportDTO();
    reportDTO.setNick("");
    reportDTO.setText("");

    Mockito.when(emailService.sendReport(any(ReportDTO.class))).thenReturn(true);

    mockMvc
        .perform(
            post("/reports")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportDTO)))
        .andExpect(status().isNotAcceptable());
  }

  @Test
  @WithMockUser(roles = "")
  public void shouldStatusNotAcceptableWhenErrorSendingTest() throws Exception {
    ReportDTO reportDTO = new ReportDTO();
    reportDTO.setNick("Antek");
    reportDTO.setText("tak");

    Mockito.when(emailService.sendReport(any(ReportDTO.class))).thenReturn(false);

    MvcResult mvcResult =
        mockMvc
            .perform(
                post("/reports")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reportDTO)))
            .andExpect(status().isNotAcceptable())
            .andReturn();
    Assertions.assertEquals(
        "error with sending report", mvcResult.getResponse().getContentAsString());
  }
}
