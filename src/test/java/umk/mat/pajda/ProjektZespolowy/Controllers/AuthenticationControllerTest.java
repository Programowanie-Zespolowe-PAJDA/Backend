package umk.mat.pajda.ProjektZespolowy.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.controllers.AuthenticationController;
import umk.mat.pajda.ProjektZespolowy.entity.Token;
import umk.mat.pajda.ProjektZespolowy.services.AuthenticationService;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.TokenService;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AuthenticationService authenticationService;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean private JWTService jwtService;

  @MockBean private RestTemplate restTemplate;

  @MockBean private TokenService tokenService;

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
  public void authenticationControllerTestRegisterStatus() throws Exception {
    // Given
    RegisterDTO user = new RegisterDTO();
    user.setMail("test@gmail.com");
    user.setName("Adrian");
    user.setSurname("Kowalski");
    user.setLocation("test");
    user.setPassword("vYjhpLpM9Bdm!");
    user.setRetypedPassword("vYjhpLpM9Bdm!");

    // When
    when(authenticationService.getUser(any(String.class))).thenReturn(null);
    when(authenticationService.register(any(RegisterDTO.class))).thenReturn(true);

    // Then
    mockMvc
        .perform(
            post("/register")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "")
  public void authenticationControllerTestLoginStatus() throws Exception {
    // Given
    LoginDTO user = new LoginDTO();
    JWTAuthenticationResponseDTO jwt = new JWTAuthenticationResponseDTO();

    // When
    when(authenticationService.login(any(LoginDTO.class))).thenReturn(jwt);

    // Then
    mockMvc
        .perform(
            post("/login")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(roles = "")
  public void authenticationControllerTestRefreshStatus() throws Exception {
    // Given
    RefreshTokenDTO token = new RefreshTokenDTO();
    JWTAuthenticationResponseDTO jwt = new JWTAuthenticationResponseDTO();

    // When
    when(authenticationService.refreshToken(any(RefreshTokenDTO.class))).thenReturn(jwt);

    // Then
    mockMvc
        .perform(
            post("/refresh")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(token)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "")
  public void authenticationControllerTestConfirmVerificationTokenStatusNotFoundWhenTokenNotFound() throws Exception {
    String token = "token";
    when(tokenService.getToken(token)).thenReturn(null);

    mockMvc.perform(get("/confirm?token=token")).andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(roles = "")
  public void authenticationControllerTestConfirmVerificationTokenStatusUnathorizedWhenTokenIsExpired() throws Exception {
    String token = "token";
    Token confirmToken = new Token();
    when(tokenService.getToken(token)).thenReturn(confirmToken);
    when(tokenService.isExpired(confirmToken)).thenReturn(true);

    mockMvc.perform(get("/confirm?token=token")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "")
  public void authenticationControllerTestConfirmVerificationTokenStatusOk() throws Exception {
    String token = "token";
    Token confirmToken = new Token();
    when(tokenService.getToken(token)).thenReturn(confirmToken);
    when(tokenService.isExpired(confirmToken)).thenReturn(false);
    when(tokenService.confirm(confirmToken)).thenReturn(true);

    mockMvc.perform(get("/confirm?token=token")).andExpect(status().isOk());
  }
}
