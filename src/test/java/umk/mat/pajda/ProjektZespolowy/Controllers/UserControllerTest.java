package umk.mat.pajda.ProjektZespolowy.Controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.controllers.UserController;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.UserService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private UserService userService;

  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean private JWTService jwtService;

  @MockBean private AuthenticationServiceImpl authenticationService;

  @MockBean private RestTemplate restTemplate;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "ADMIN")
  public void userControllerTestGetAllUsersStatus() throws Exception {
    mockMvc.perform(get("/user")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void userControllerTestGetAllUsersIsValidJson() throws Exception {
    ResultActions result = mockMvc.perform(get("/user"));
    String content = result.andReturn().getResponse().getContentAsString();
    // Asserting that the content is valid JSON
    assertDoesNotThrow(() -> objectMapper.readTree(content));
  }

  @Test
  @WithMockUser(roles = "USER")
  public void userControllerTestGetOwnerStatus() throws Exception {
    mockMvc.perform(get("/user/profile")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void userControllerTestGetOwnerIsValidJson() throws Exception {
    ResultActions result = mockMvc.perform(get("/user/profile"));
    String content = result.andReturn().getResponse().getContentAsString();
    // Asserting that the content is valid JSON
    assertDoesNotThrow(() -> objectMapper.readTree(content));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void userControllerTestGetSelectedUserStatus() throws Exception {
    mockMvc.perform(get("/user/1")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void userControllerTestGetSelectedUserIsValidJson() throws Exception {
    ResultActions result = mockMvc.perform(get("/user/1"));
    String content = result.andReturn().getResponse().getContentAsString();
    // Asserting that the content is valid JSON
    assertDoesNotThrow(() -> objectMapper.readTree(content));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void userControllerTestDelUserStatus() throws Exception {
    // When
    when(userService.deleteSelectedUser(any(int.class))).thenReturn(true);
    // Then
    mockMvc
        .perform(delete("/user/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void userControllerTestModInformationsOfUserStatus() throws Exception {
    mockMvc
        .perform(patch("/user/editInformations").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void userControllerTestModPasswordOfUserStatus() throws Exception {
    mockMvc
        .perform(patch("/user/editPassword").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void userControllerTestModEmailOfUserStatus() throws Exception {
    mockMvc
        .perform(patch("/user/editEmail").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void userControllerTestModBankAccountNumberOfUserStatus() throws Exception {
    mockMvc
        .perform(
            patch("/user/editBankAccountNumber").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk());
  }
}
