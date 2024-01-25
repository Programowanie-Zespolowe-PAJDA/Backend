package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPutPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
public class ReviewService {

  private final Logger logger = LoggerFactory.getLogger(ReviewService.class);
  private final ReviewRepository reviewRepository;
  private final ReviewConverter reviewConverter;

  private final UserRepository userRepository;

  @Autowired
  public ReviewService(
      ReviewRepository reviewRepository,
      ReviewConverter reviewConverter,
      UserRepository userRepository) {
    this.reviewRepository = reviewRepository;
    this.reviewConverter = reviewConverter;
    this.userRepository = userRepository;
  }

  public Boolean addReview(ReviewPutPostDTO reviewPutPostDTO) {
    try {
      reviewRepository.save(reviewConverter.createEntity(reviewPutPostDTO));
    } catch (Exception e) {
      logger.error("addReview", e);
      return false;
    }
    return true;
  }

  public List<ReviewGetDTO> getAllReviews() {
    return reviewConverter.createReviewDTOList(reviewRepository.findAll());
  }

  public List<ReviewGetDTO> getAllReviews(String email) {
    Optional<User> user = userRepository.findByMail(email);
    if (user.isPresent()) {
      return reviewConverter.createReviewDTOList(reviewRepository.findAllByUser(user.get()));
    }
    return null;
  }

  public ReviewGetDTO getReview(int id) {
    Review review = null;
    try {
      review = reviewRepository.findById(id).get();
    } catch (NoSuchElementException e) {
      logger.error("getReview", e);
      return null;
    }
    if (review == null) {
      return null;
    }
    return reviewConverter.createDTO(review);
  }

  public ReviewGetDTO getReview(int id, String email) {
    Review review = null;
    try {
      review = reviewRepository.findByIdAndUser(id, userRepository.findByMail(email).get());
    } catch (NoSuchElementException e) {
      logger.error("getReview", e);
      return null;
    }
    if (review == null) {
      return null;
    }
    return reviewConverter.createDTO(review);
  }

  public Boolean deleteSelectReview(int id) {
    try {
      reviewRepository.deleteById(id);
    } catch (Exception e) {
      logger.error("deleteSelectReview", e);
      return false;
    }
    return true;
  }

  public Boolean patchSelectReview(ReviewPutPostDTO reviewPutPostDTO, int id) {
    try {
      Review review = reviewRepository.findById(id).get();
      review.setClientName(reviewPutPostDTO.getClientName());
      review.setComment(reviewPutPostDTO.getComment());
      review.setRating(reviewPutPostDTO.getRating());
      reviewRepository.save(review);
    } catch (Exception e) {
      logger.error("patchSelectReview", e);
      return false;
    }
    return true;
  }
}
