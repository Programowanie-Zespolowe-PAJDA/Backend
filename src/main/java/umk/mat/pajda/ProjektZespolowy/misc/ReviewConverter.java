package umk.mat.pajda.ProjektZespolowy.misc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class ReviewConverter {

  private final UserRepository userRepository;

  @Autowired
  public ReviewConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ReviewDTO createDTO(Review review) {
    ReviewDTO reviewDTO = new ReviewDTO();
    reviewDTO.setCreatedAt(review.getCreatedAt());
    reviewDTO.setRating(review.getRating());
    reviewDTO.setComment(review.getComment());
    reviewDTO.setClientName(review.getClientName());
    return reviewDTO;
  }

  public Review createEntity(ReviewDTO reviewDTO) {
    Review review = new Review();
    review.setRating(reviewDTO.getRating());
    review.setComment(reviewDTO.getComment());
    review.setCreatedAt(LocalDateTime.now());
    review.setUser(userRepository.findById(reviewDTO.getUserID()).get());
    review.setClientName(reviewDTO.getClientName());
    review.setHashRevID(reviewDTO.getHashRevID());
    return review;
  }

  public List<ReviewDTO> createReviewDTOList(List<Review> list) {
    List<ReviewDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
