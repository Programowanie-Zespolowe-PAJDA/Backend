package umk.mat.pajda.ProjektZespolowy.misc;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;

@Component
public class ReviewConverter {

  private final UserConverter userConverter;

  @Autowired
  public ReviewConverter(UserConverter userConverter) {
    this.userConverter = userConverter;
    userConverter.setReviewConverter(this);
  }

  public ReviewDTO createDTO(Review review) {
    ReviewDTO reviewDTO = new ReviewDTO();
    reviewDTO.setId(review.getId());
    reviewDTO.setRating(review.getRating());
    reviewDTO.setComment(review.getComment());

    reviewDTO.setReviewTimeStamp(review.getReviewTimeStamp());
    reviewDTO.setUser(userConverter.createDTO(review.getUser()));
    reviewDTO.setClientName(review.getClientName());
    reviewDTO.setHashRevID(review.getHashRevID());

    return reviewDTO;
  }

  public Review createEntity(ReviewDTO reviewDTO) {
    Review review = new Review();
    review.setId(reviewDTO.getId());
    review.setRating(reviewDTO.getRating());
    review.setComment(reviewDTO.getComment());
    review.setReviewTimeStamp(reviewDTO.getReviewTimeStamp());
    review.setUser(userConverter.createEntity(reviewDTO.getUser()));
    review.setClientName(reviewDTO.getClientName());
    review.setHashRevID(reviewDTO.getHashRevID());
    return review;
  }

  public List<ReviewDTO> createReviewDTOList(List<Review> list) {
    List<ReviewDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
