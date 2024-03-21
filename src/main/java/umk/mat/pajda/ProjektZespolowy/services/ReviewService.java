package umk.mat.pajda.ProjektZespolowy.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.OpinionPostDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewAvgRatingGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewPatchDTO;
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

  public Boolean addReview(OpinionPostDTO opinionPostDTO, String id) {
    try {
      reviewRepository.save(reviewConverter.createEntity(opinionPostDTO, id));
    } catch (Exception e) {
      logger.error("addReview", e);
      return false;
    }
    return true;
  }

  public List<ReviewGetDTO> getAllReviews() {
    return reviewConverter.createReviewDTOList(reviewRepository.findAllByEnabledIsTrue());
  }

  public List<ReviewGetDTO> getAllReviews(String email) {
    Optional<User> user = userRepository.findByMail(email);
    if (user.isPresent()) {
      return reviewConverter.createReviewDTOList(
          reviewRepository.findAllByUserAndEnabledIsTrue(user.get()));
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

  public ReviewGetDTO getReview(String id, String email) {
    Review review = null;
    try {
      review =
          reviewRepository.findByIdAndUserAndEnabledIsTrue(
              id, userRepository.findByMail(email).get());
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

  public Boolean patchSelectReview(ReviewPatchDTO reviewPatchDTO, int id) {
    try {
      Review review = reviewRepository.findById(id).get();
      review.setClientName(reviewPatchDTO.getClientName());
      review.setComment(reviewPatchDTO.getComment());
      review.setRating(reviewPatchDTO.getRating());
      reviewRepository.save(review);
    } catch (Exception e) {
      logger.error("patchSelectReview", e);
      return false;
    }
    return true;
  }

  public boolean validateTime(OpinionPostDTO opinionPostDTO) {
    Review review = null;
    LocalDateTime currentDateTime = LocalDateTime.now();
    try {
      review =
          reviewRepository.findFirstByUserAndEnabledIsTrueAndHashRevIDOrderByCreatedAtDesc(
              userRepository.findById(opinionPostDTO.getUserID()).get(),
              opinionPostDTO.getHashRevID());
      logger.info(String.valueOf(review));
      if (review == null) {
        return true;
      }
      long minutesDifference = ChronoUnit.MINUTES.between(review.getCreatedAt(), currentDateTime);
      logger.info("Time in min:" + minutesDifference);
      return Math.abs(minutesDifference) >= 10;

    } catch (Exception e) {
      logger.error("validateTime", e);
      return false;
    }
  }

  public ReviewAvgRatingGetDTO getAvgRatingOfReview(String username) {
    try {
      ReviewAvgRatingGetDTO reviewAvgRatingGetDTO = new ReviewAvgRatingGetDTO();
      reviewAvgRatingGetDTO.setAvgRating(reviewRepository.getAvgRating(username));
      return reviewAvgRatingGetDTO;
    } catch (Exception e) {
      logger.error("getAvgRatingOfReview", e);
      return null;
    }
  }
}
