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
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewArchiveGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewArchivePatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.ReviewArchive;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewArchiveConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@ActiveProfiles("tests")
public class ReviewArchiveConverterTest {

  private ReviewArchiveConverter reviewArchiveConverter;

  @Mock private UserRepository userRepository;

  @BeforeEach
  public void setUp() throws Exception {
    openMocks(this);

    reviewArchiveConverter = new ReviewArchiveConverter(userRepository);
  }

  @Test
  public void shouldCreateReviewArchiveGetDTO() {
    LocalDateTime localDateTime = LocalDateTime.now();

    User user = new User();

    user.setSurname("Nowak");
    user.setId(123);
    user.setName("Tomek");
    user.setMail("wojtek@mail.com");

    ReviewArchive reviewArchive = new ReviewArchive();
    reviewArchive.setRating(122);
    reviewArchive.setComment("Abc dcf jgs torba");
    reviewArchive.setCreatedAt(localDateTime);
    reviewArchive.setClientName("Wojtek");
    reviewArchive.setId(123);
    reviewArchive.setHashRevID("13231");
    reviewArchive.setUser(user);

    ReviewArchiveGetDTO reviewArchiveDTO = reviewArchiveConverter.createDTO(reviewArchive);

    ReviewArchiveGetDTO reviewArchiveDTOShouldBe = new ReviewArchiveGetDTO();
    reviewArchiveDTOShouldBe.setRating(122);
    reviewArchiveDTOShouldBe.setComment("Abc dcf jgs torba");
    reviewArchiveDTOShouldBe.setCreatedAt(localDateTime);
    reviewArchiveDTOShouldBe.setClientName("Wojtek");
    reviewArchiveDTOShouldBe.setUserID(123);

    Assertions.assertEquals(reviewArchiveDTOShouldBe.toString(), reviewArchiveDTO.toString());
  }

  @Test
  public void shouldCreateReviewArchive() {
    ReviewArchivePatchPostDTO reviewArchiveDTO = new ReviewArchivePatchPostDTO();
    reviewArchiveDTO.setRating(122);

    reviewArchiveDTO.setComment("Abc dcf jgs torba");
    reviewArchiveDTO.setClientName("Wojtek");
    reviewArchiveDTO.setHashRevID("12333");

    reviewArchiveDTO.setUserID(231);

    User user = new User();

    user.setSurname("Nowak");
    user.setName("Tomek");
    user.setMail("wojtek@mail.com");

    when(userRepository.findById(231)).thenReturn(Optional.of(user));

    ReviewArchive reviewArchive = reviewArchiveConverter.createEntity(reviewArchiveDTO);

    ReviewArchive reviewArchiveShouldBe = new ReviewArchive();

    reviewArchiveShouldBe.setRating(122);
    reviewArchiveShouldBe.setComment("Abc dcf jgs torba");
    reviewArchiveShouldBe.setClientName("Wojtek");
    reviewArchiveShouldBe.setHashRevID("12333");
    reviewArchiveShouldBe.setUser(user);

    Assertions.assertEquals(reviewArchiveShouldBe.toString2(), reviewArchive.toString2());
  }

  @Test
  public void shouldCreateReviewArchiveGetDTOList() {
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

    ReviewArchive reviewArchive1 = new ReviewArchive();
    ReviewArchive reviewArchive2 = new ReviewArchive();
    ReviewArchive reviewArchive3 = new ReviewArchive();

    reviewArchive1.setRating(122);
    reviewArchive1.setId(1122);
    reviewArchive1.setComment("Abc dcf jgs torba");
    reviewArchive1.setClientName("Wojtek");
    reviewArchive1.setHashRevID("12333");
    reviewArchive1.setCreatedAt(localDateTime1);
    reviewArchive1.setUser(user1);

    reviewArchive2.setRating(432);
    reviewArchive2.setId(333);
    reviewArchive2.setComment("bbbbbb");
    reviewArchive2.setClientName("Nikol");
    reviewArchive2.setHashRevID("6565");
    reviewArchive2.setCreatedAt(localDateTime2);
    reviewArchive2.setUser(user2);

    reviewArchive3.setRating(555);
    reviewArchive3.setId(5);
    reviewArchive3.setComment("fgdsg");
    reviewArchive3.setClientName("Milosz");
    reviewArchive3.setHashRevID("5555");
    reviewArchive3.setCreatedAt(localDateTime3);
    reviewArchive3.setUser(user3);

    List<ReviewArchive> listReviewArchive = List.of(reviewArchive1, reviewArchive2, reviewArchive3);

    List<ReviewArchiveGetDTO> listReviewArchiveGetDTO =
        reviewArchiveConverter.createReviewArchiveDTOList(listReviewArchive);

    ReviewArchiveGetDTO reviewArchiveDTO1 = new ReviewArchiveGetDTO();
    ReviewArchiveGetDTO reviewArchiveDTO2 = new ReviewArchiveGetDTO();
    ReviewArchiveGetDTO reviewArchiveDTO3 = new ReviewArchiveGetDTO();

    reviewArchiveDTO1.setRating(122);
    reviewArchiveDTO1.setComment("Abc dcf jgs torba");
    reviewArchiveDTO1.setCreatedAt(localDateTime1);
    reviewArchiveDTO1.setClientName("Wojtek");
    reviewArchiveDTO1.setUserID(1);

    reviewArchiveDTO2.setRating(432);
    reviewArchiveDTO2.setComment("bbbbbb");
    reviewArchiveDTO2.setCreatedAt(localDateTime2);
    reviewArchiveDTO2.setClientName("Nikol");
    reviewArchiveDTO2.setUserID(2);

    reviewArchiveDTO3.setRating(555);
    reviewArchiveDTO3.setComment("fgdsg");
    reviewArchiveDTO3.setCreatedAt(localDateTime3);
    reviewArchiveDTO3.setClientName("Milosz");
    reviewArchiveDTO3.setUserID(3);

    List<ReviewArchiveGetDTO> listReviewArchiveGetDTOShouldBe =
        List.of(reviewArchiveDTO1, reviewArchiveDTO2, reviewArchiveDTO3);

    Assertions.assertEquals(
        listReviewArchiveGetDTO.get(0).toString(),
        listReviewArchiveGetDTOShouldBe.get(0).toString());
    Assertions.assertEquals(
        listReviewArchiveGetDTO.get(1).toString(),
        listReviewArchiveGetDTOShouldBe.get(1).toString());
    Assertions.assertEquals(
        listReviewArchiveGetDTO.get(2).toString(),
        listReviewArchiveGetDTOShouldBe.get(2).toString());
  }
}
