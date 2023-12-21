package umk.mat.pajda.ProjektZespolowy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void testUnauthenticatedAccessToHello() throws Exception {
    mockMvc.perform(get("/hello")).andExpect(status().isOk());
  }

  @Test
  void testUnauthenticatedAccessToAuthenticated() throws Exception {
    mockMvc.perform(get("/authenticated")).andExpect(status().isFound());
  }

  @Test
  void testUnauthenticatedAccessToAdmin() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isFound());
  }

  @Test
  @WithMockUser(roles = "USER")
  void testAuthenticatedAccessToHello() throws Exception {
    mockMvc.perform(get("/hello")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  void testAuthenticatedAccessToAuthenticated() throws Exception {
    mockMvc.perform(get("/authenticated")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  void testAuthenticatedAccessToAdmin() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAdminAccessToHello() throws Exception {
    mockMvc.perform(get("/hello")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAdminAccessToAuthenticated() throws Exception {
    mockMvc.perform(get("/authenticated")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAdminAccessToAdmin() throws Exception {
    mockMvc.perform(get("/admin")).andExpect(status().isOk());
  }
}
