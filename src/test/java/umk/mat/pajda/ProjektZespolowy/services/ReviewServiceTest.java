package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.misc.Status;
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
  public void shouldSuccessWhenSetStatusTest() {
    Review review = new Review();
    review.setId("reviewId");
    review.setStatus(Status.PENDING);

    Mockito.when(reviewRepository.findById("reviewId")).thenReturn(Optional.of(review));

    Assertions.assertTrue(reviewService.setStatus("reviewId", Status.COMPLETED));
  }

  @Test
  public void shouldFailWhenSetStatusTest() {
    Mockito.when(reviewRepository.findById("reviewId")).thenReturn(null);

    Assertions.assertFalse(reviewService.setStatus("orderId", Status.PENDING));
  }

  @Test
  public void shouldSuccessWhenGetUserTest() {
    User user = new User();

    Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

    Assertions.assertEquals(user, reviewService.getUser(1));
  }

  @Test
  public void shouldFailWhenGetUserTest() {
    Mockito.when(userRepository.findById(1)).thenReturn(null);

    Assertions.assertNull(reviewService.getUser(1));
  }

  @Test
  public void shouldSuccessWhenGetReviewById() {
    Review review = new Review();

    Mockito.when(reviewRepository.findById("id")).thenReturn(Optional.of(review));

    Assertions.assertEquals(review, reviewService.getReviewById("id"));
  }

  @Test
  public void shouldFailWhenGetReviewById() {
    Mockito.when(reviewRepository.findById("id")).thenReturn(null);

    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          reviewService.getReviewById("id");
        });
  }

  @Test
  public void shouldSuccessWhenGetAllReviewsByEmailTest() {
    User user = new User();
    List<Review> reviews = List.of(new Review(), new Review());

    Mockito.when(userRepository.findByMail("email")).thenReturn(Optional.of(user));
    Mockito.when(reviewRepository.findAllByUserAndStatus(user, Status.COMPLETED))
        .thenReturn(reviews);
    Assertions.assertEquals(reviews, reviewService.getAllReviewsByEmail("email"));
  }

  @Test
  public void shouldFailWhenGetAllReviewsByEmailTest() {
    Mockito.when(userRepository.findByMail("email")).thenReturn(null);

    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          reviewService.getAllReviewsByEmail("email");
        });
  }
}
