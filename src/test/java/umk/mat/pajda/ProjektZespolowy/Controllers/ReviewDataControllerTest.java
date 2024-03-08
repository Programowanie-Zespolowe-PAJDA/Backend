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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPatchDTO;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.controllers.ReviewDataController;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

@WebMvcTest(ReviewDataController.class)
@AutoConfigureMockMvc
@Profile("!tests")
public class ReviewDataControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ReviewService reviewService;

  @MockBean private AuthenticationManager authenticationManager;

  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean private JWTService jwtService;

  @MockBean private AuthenticationServiceImpl authenticationService;

  @MockBean private RestTemplate restTemplate;

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
  @WithMockUser(roles = "ADMIN")
  public void reviewControllerTestModReviewStatus() throws Exception {
    // Given
    ReviewPatchDTO review = new ReviewPatchDTO();
    review.setComment("test");
    review.setRating(5);
    review.setClientName("asdasdas");
    review.setHashRevID("aasdad");
    review.setUserID(5);
    ReviewGetDTO reviewGetDTO = new ReviewGetDTO();

    // When
    when(reviewService.getReview(any(int.class))).thenReturn(reviewGetDTO);
    when(reviewService.patchSelectReview(any(ReviewPatchDTO.class), any(int.class)))
        .thenReturn(true);

    // Then
    mockMvc
        .perform(
            patch("/review/5")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "")
  public void reviewControllerTestGetAllReviewsStatus() throws Exception {
    mockMvc.perform(get("/review")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "")
  public void reviewControllerTestGetAllReviewsIsValidJson() throws Exception {
    ResultActions result = mockMvc.perform(get("/review"));
    String content = result.andReturn().getResponse().getContentAsString();
    // Asserting that the content is valid JSON
    assertDoesNotThrow(() -> objectMapper.readTree(content));
  }

  @Test
  @WithMockUser(roles = "USER")
  public void reviewControllerTestGetAllReviewsOfUserStatus() throws Exception {
    mockMvc.perform(get("/review/owner")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void reviewControllerTestGetAllReviewsOfUserIsValidJson() throws Exception {
    ResultActions result = mockMvc.perform(get("/review/owner"));
    String content = result.andReturn().getResponse().getContentAsString();
    // Asserting that the content is valid JSON
    assertDoesNotThrow(() -> objectMapper.readTree(content));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void reviewControllerTestGetSelectedReviewStatus() throws Exception {
    // When
    when(reviewService.getReview(any(Integer.class))).thenReturn(new ReviewGetDTO());
    // Then
    mockMvc.perform(get("/review/1")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void reviewControllerTestGetSelectedReviewIsValidJson() throws Exception {
    ResultActions result = mockMvc.perform(get("/review/1"));
    String content = result.andReturn().getResponse().getContentAsString();
    // Asserting that the content is valid JSON
    assertDoesNotThrow(() -> objectMapper.readTree(content));
  }

  @Test
  @WithMockUser(roles = "USER")
  public void reviewControllerTestGetSelectedReviewOfUserStatus() throws Exception {
    // When
    when(reviewService.getReview(any(Integer.class), any(String.class)))
        .thenReturn(new ReviewGetDTO());
    // Then
    mockMvc.perform(get("/review/owner/1")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  public void reviewControllerTestGetSelectedReviewOfUserIsValidJson() throws Exception {
    ResultActions result = mockMvc.perform(get("/review/owner/1"));
    String content = result.andReturn().getResponse().getContentAsString();
    // Asserting that the content is valid JSON
    assertDoesNotThrow(() -> objectMapper.readTree(content));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void reviewControllerTestDelReviewStatus() throws Exception {
    // When
    when(reviewService.deleteSelectReview(any(Integer.class))).thenReturn(true);
    // Then
    mockMvc
        .perform(delete("/review/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk());
  }
}
