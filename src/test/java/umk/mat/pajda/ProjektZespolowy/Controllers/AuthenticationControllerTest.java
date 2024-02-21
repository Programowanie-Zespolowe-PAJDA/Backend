package umk.mat.pajda.ProjektZespolowy.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.controllers.AuthenticationController;
import umk.mat.pajda.ProjektZespolowy.services.AuthenticationService;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AuthenticationService authenticationService;


  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean private JWTService jwtService;

  @Autowired private ObjectMapper objectMapper;



  @Test
  @WithMockUser(roles = "")
  public void authenticationControllerTest_register_status() throws Exception {
    // Given
    RegisterDTO user = new RegisterDTO();
    user.setPassword("test");
    user.setRetypedPassword("test");

    // When
    when(authenticationService.getUser(any(String.class))).thenReturn(false);
    when(authenticationService.register(any(RegisterDTO.class))).thenReturn(false);

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
  public void authenticationControllerTest_login_status() throws Exception {
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
  public void authenticationControllerTest_refresh_status() throws Exception {
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
        .andExpect(status().isCreated());
  }
}
