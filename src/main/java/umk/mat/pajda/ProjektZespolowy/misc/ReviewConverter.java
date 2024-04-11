package umk.mat.pajda.ProjektZespolowy.misc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class ReviewConverter {

  private final UserRepository userRepository;

  @Autowired
  public ReviewConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ReviewGetDTO createDTO(Review review) {
    ReviewGetDTO reviewGetDTO = new ReviewGetDTO();
    reviewGetDTO.setId(review.getId());
    reviewGetDTO.setCreatedAt(review.getCreatedAt());
    reviewGetDTO.setRating(review.getRating());
    reviewGetDTO.setComment(review.getComment());
    reviewGetDTO.setClientName(review.getClientName());
    reviewGetDTO.setUserID(review.getUser().getId());
    return reviewGetDTO;
  }

  public Review createEntity(OpinionPostDTO opinionPostDTO, String id) {
    Review review = new Review();
    review.setId(id);
    review.setRating(opinionPostDTO.getRating());
    review.setComment(opinionPostDTO.getComment());
    review.setCreatedAt(LocalDateTime.now());
    review.setUser(userRepository.findById(opinionPostDTO.getUserID()).get());
    review.setClientName(opinionPostDTO.getClientName());
    review.setHashRevID(opinionPostDTO.getHashRevID());
    review.setStatus(Status.PENDING);
    return review;
  }

  public List<ReviewGetDTO> createReviewDTOList(List<Review> list) {
    List<ReviewGetDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
