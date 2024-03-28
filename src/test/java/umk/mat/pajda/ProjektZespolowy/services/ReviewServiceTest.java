package umk.mat.pajda.ProjektZespolowy.services;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

public class ReviewServiceTest {
  @Mock private ReviewRepository reviewRepository;
  @Mock private ReviewConverter reviewConverter;
  @Mock private UserRepository userRepository;

  @InjectMocks private ReviewService reviewService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldSuccessWhenSetEnabledTest() {
    Review review = new Review();
    review.setId("reviewId");
    review.setEnabled(false);

    Mockito.when(reviewRepository.findById("reviewId")).thenReturn(Optional.of(review));

    Assertions.assertTrue(reviewService.setEnabled("reviewId"));
  }

  @Test
  public void shouldFailWhenSetEnabledTest() {
    Mockito.when(reviewRepository.findById("reviewId")).thenReturn(null);

    Assertions.assertFalse(reviewService.setEnabled("reviewId"));
  }
}
