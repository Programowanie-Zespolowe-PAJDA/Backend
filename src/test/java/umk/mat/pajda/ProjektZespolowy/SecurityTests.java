package umk.mat.pajda.ProjektZespolowy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class SecurityConfigTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldIsOkWhenUnauthenticatedAccessToHelloTest() throws Exception {
    mockMvc.perform(get("/hello")).andExpect(status().isOk());
  }

  @Test
  void shouldIsFoundWhenUnauthenticatedAccessToAuthenticatedTest() throws Exception {
    mockMvc.perform(get("/authenticated")).andExpect(status().isFound());
  }

  @Test
  void shouldIsFoundWhenUnauthenticatedAccessToAdminTest() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isFound());
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldIsOkWhenAuthenticatedAccessToHelloTest() throws Exception {
    mockMvc.perform(get("/hello")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldIsOkWhenAuthenticatedAccessToAuthenticatedTest() throws Exception {
    mockMvc.perform(get("/authenticated")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldIsForbiddenWhenAuthenticatedAccessToAdminTest() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldIsOkWhenAdminAccessToHelloTest() throws Exception {
    mockMvc.perform(get("/hello")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldIsOkWhenAdminAccessToAuthenticatedTest() throws Exception {
    mockMvc.perform(get("/authenticated")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldIsOkWhenAdminAccessToAdminTest() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isOk());
  }
}
