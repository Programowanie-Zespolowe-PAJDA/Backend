package umk.mat.pajda.ProjektZespolowy.converters;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.misc.Status;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@ActiveProfiles("tests")
public class ReviewConverterTest {
  private ReviewConverter reviewConverter;

  @Mock private UserRepository userRepository;

  @BeforeEach
  public void setUp() throws Exception {
    openMocks(this);

    reviewConverter = new ReviewConverter(userRepository);
  }

  @Test
  public void shouldCreateReviewDTO() {
    LocalDateTime localDateTime = LocalDateTime.now();

    User user = new User();

    user.setSurname("Nowak");
    user.setId(123);
    user.setName("Tomek");
    user.setMail("wojtek@mail.com");

    Review review = new Review();
    review.setRating(122);
    review.setComment("Abc dcf jgs torba");
    review.setCreatedAt(localDateTime);
    review.setClientName("Wojtek");
    review.setId("123");
    review.setHashRevID("13231");
    review.setUser(user);

    ReviewGetDTO reviewDTO = reviewConverter.createDTO(review);

    ReviewGetDTO reviewDTOShouldBe = new ReviewGetDTO();
    reviewDTOShouldBe.setRating(122);
    reviewDTOShouldBe.setId("123");
    reviewDTOShouldBe.setComment("Abc dcf jgs torba");
    reviewDTOShouldBe.setCreatedAt(localDateTime);
    reviewDTOShouldBe.setClientName("Wojtek");
    reviewDTOShouldBe.setUserID(123);

    Assertions.assertEquals(reviewDTOShouldBe.toString(), reviewDTO.toString());
  }

  @Test
  public void shouldCreateReview() {
    OpinionPostDTO opinionPostDTO = new OpinionPostDTO();
    opinionPostDTO.setRating(122);
    opinionPostDTO.setComment("Abc dcf jgs torba");
    opinionPostDTO.setClientName("Wojtek");
    opinionPostDTO.setHashRevID("12333");
    opinionPostDTO.setUserID(231);
    opinionPostDTO.setAmount(111);
    opinionPostDTO.setCurrency("PLN");

    User user = new User();

    user.setSurname("Nowak");
    user.setName("Tomek");
    user.setMail("wojtek@mail.com");

    when(userRepository.findById(231)).thenReturn(Optional.of(user));

    Review review = reviewConverter.createEntity(opinionPostDTO,"888");

    Review reviewShouldBe = new Review();

    reviewShouldBe.setRating(122);
    reviewShouldBe.setId("888");
    reviewShouldBe.setComment("Abc dcf jgs torba");
    reviewShouldBe.setClientName("Wojtek");
    reviewShouldBe.setHashRevID("12333");
    reviewShouldBe.setUser(user);
    reviewShouldBe.setStatus(Status.PENDING);

    Assertions.assertEquals(reviewShouldBe.toString2(), review.toString2());
  }

  @Test
  public void shouldCreateReviewArchiveDTOList() {
    LocalDateTime localDateTime1 = LocalDateTime.of(123, 3, 2, 3, 3);
    LocalDateTime localDateTime2 = LocalDateTime.of(1233, 1, 4, 2, 3);
    LocalDateTime localDateTime3 = LocalDateTime.of(1, 8, 2, 5, 3);

    User user1 = new User();
    User user2 = new User();
    User user3 = new User();

    user1.setSurname("Nowak");
    user1.setId(1);
    user1.setName("Tomek");
    user1.setMail("wojtek@mail.com");

    user2.setSurname("Lewandowski");
    user2.setId(2);
    user2.setName("Marek");
    user2.setMail("marek@mail.com");

    user3.setSurname("Nikolas");
    user3.setId(3);
    user3.setName("Tom");
    user3.setMail("tom@mail.com");

    Review review1 = new Review();
    Review review2 = new Review();
    Review review3 = new Review();

    review1.setRating(122);
    review1.setId("1122");
    review1.setComment("Abc dcf jgs torba");
    review1.setClientName("Wojtek");
    review1.setHashRevID("12333");
    review1.setCreatedAt(localDateTime1);
    review1.setUser(user1);

    review2.setRating(432);
    review2.setId("333");
    review2.setComment("bbbbbb");
    review2.setClientName("Nikol");
    review2.setHashRevID("6565");
    review2.setCreatedAt(localDateTime2);
    review2.setUser(user2);

    review3.setRating(555);
    review3.setId("5");
    review3.setComment("fgdsg");
    review3.setClientName("Milosz");
    review3.setHashRevID("5555");
    review3.setCreatedAt(localDateTime3);
    review3.setUser(user3);

    List<Review> listReview = List.of(review1, review2, review3);

    List<ReviewGetDTO> listReviewDTO = reviewConverter.createReviewDTOList(listReview);

    ReviewGetDTO reviewDTO1 = new ReviewGetDTO();
    ReviewGetDTO reviewDTO2 = new ReviewGetDTO();
    ReviewGetDTO reviewDTO3 = new ReviewGetDTO();

    reviewDTO1.setRating(122);
    reviewDTO1.setId("1122");
    reviewDTO1.setComment("Abc dcf jgs torba");
    reviewDTO1.setCreatedAt(localDateTime1);
    reviewDTO1.setClientName("Wojtek");
    reviewDTO1.setUserID(1);

    reviewDTO2.setRating(432);
    reviewDTO2.setId("333");
    reviewDTO2.setComment("bbbbbb");
    reviewDTO2.setCreatedAt(localDateTime2);
    reviewDTO2.setClientName("Nikol");
    reviewDTO2.setUserID(2);

    reviewDTO3.setRating(555);
    reviewDTO3.setId("5");
    reviewDTO3.setComment("fgdsg");
    reviewDTO3.setCreatedAt(localDateTime3);
    reviewDTO3.setClientName("Milosz");
    reviewDTO3.setUserID(3);

    List<ReviewGetDTO> listReviewDTOShouldBe = List.of(reviewDTO1, reviewDTO2, reviewDTO3);

    Assertions.assertEquals(
        listReviewDTO.get(0).toString(), listReviewDTOShouldBe.get(0).toString());
    Assertions.assertEquals(
        listReviewDTO.get(1).toString(), listReviewDTOShouldBe.get(1).toString());
    Assertions.assertEquals(
        listReviewDTO.get(2).toString(), listReviewDTOShouldBe.get(2).toString());
  }
}
