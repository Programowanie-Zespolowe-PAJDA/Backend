package umk.mat.pajda.ProjektZespolowy.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.configs.JwtAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.controllers.ReviewDataController;

import umk.mat.pajda.ProjektZespolowy.services.JWTService;
import umk.mat.pajda.ProjektZespolowy.services.ReviewService;
import umk.mat.pajda.ProjektZespolowy.services.impl.AuthenticationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewDataController.class)
@AutoConfigureMockMvc
public class ReviewDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Mock
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean private JWTService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @WithMockUser(roles = "")
    public void reviewControllerTest_addReview_status() throws Exception {
        // Given
        ReviewPatchPostDTO review = new ReviewPatchPostDTO();
        review.setComment("test");
        review.setRating(5);
        review.setClientName("delta");
        review.setHashRevID("adsads");
        review.setUserID(5);


        //When
        when(reviewService.addReview(any(ReviewPatchPostDTO.class))).thenReturn(false);

        //Then
        mockMvc.perform(post("/review").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void reviewControllerTest_modReview_status() throws Exception {
        // Given
        ReviewPatchPostDTO review = new ReviewPatchPostDTO();
        review.setComment("test");
        review.setRating(5);
        review.setClientName("asdasdas");
        review.setHashRevID("aasdad");
        review.setUserID(5);
        ReviewGetDTO reviewGetDTO = new ReviewGetDTO();

        //When
        when(reviewService.getReview(any(int.class))).thenReturn(reviewGetDTO);
        when(reviewService.patchSelectReview(any(ReviewPatchPostDTO.class),any(int.class))).thenReturn(true);

        //Then
        mockMvc.perform(patch("/5").with(SecurityMockMvcRequestPostProcessors.csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(roles = "")
    public void reviewControllerTest_getAllReviews_status() throws Exception {
        mockMvc.perform(get("/review")).andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "")
    public void reviewControllerTest_getAllReviews_IsValidJson() throws Exception {
        ResultActions result = mockMvc.perform(get("/review"));
        String content = result.andReturn().getResponse().getContentAsString();
        // Asserting that the content is valid JSON
        assertDoesNotThrow(() -> objectMapper.readTree(content));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void reviewControllerTest_getAllReviewsOfUser_status() throws Exception {
        mockMvc.perform(get("/review/owner")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void reviewControllerTest_getAllReviewsOfUser_IsValidJson() throws Exception {
        ResultActions result = mockMvc.perform(get("/review/owner"));
        String content = result.andReturn().getResponse().getContentAsString();
        // Asserting that the content is valid JSON
        assertDoesNotThrow(() -> objectMapper.readTree(content));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void reviewControllerTest_getSelectedReviewOfUser_status() throws Exception {
        //When
        when(reviewService.getReview(any(Integer.class))).thenReturn(new ReviewGetDTO());
        //Then
        mockMvc.perform(get("/review/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void reviewControllerTest_getSelectedReviewOfUser_IsValidJson() throws Exception {
        ResultActions result = mockMvc.perform(get("/review/owner/1"));
        String content = result.andReturn().getResponse().getContentAsString();
        // Asserting that the content is valid JSON
        assertDoesNotThrow(() -> objectMapper.readTree(content));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void reviewControllerTest_delReview_status() throws Exception {
        //When
        when(reviewService.deleteSelectReview(any(Integer.class))).thenReturn(true);
        //Then
        mockMvc.perform(delete("/review/1").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk());
    }










}
