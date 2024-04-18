package umk.mat.pajda.ProjektZespolowy.services;

import jakarta.transaction.Transactional;
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
import umk.mat.pajda.ProjektZespolowy.misc.Status;
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
    return reviewConverter.createReviewDTOList(reviewRepository.findAllByStatus(Status.COMPLETED));
  }

  public List<ReviewGetDTO> getAllReviews(String email) {
    Optional<User> user = userRepository.findByMail(email);
    if (user.isPresent()) {
      return reviewConverter.createReviewDTOList(
          reviewRepository.findAllByUserAndStatus(user.get(), Status.COMPLETED));
    }
    return null;
  }

  public ReviewGetDTO getReview(String id) {
    Review review = null;
    try {
      review = reviewRepository.findByIdAndStatus(id, Status.COMPLETED).get();
    } catch (NoSuchElementException e) {
      logger.error("getReview", e);
      return null;
    }
    return reviewConverter.createDTO(review);
  }

  public ReviewGetDTO getReview(String id, String email) {
    Review review = null;
    try {
      review =
          reviewRepository.findByIdAndUserAndStatus(
              id, userRepository.findByMail(email).get(), Status.COMPLETED);

    } catch (NoSuchElementException e) {
      logger.error("getReview", e);
      return null;
    }
    if (review == null) {
      return null;
    }
    return reviewConverter.createDTO(review);
  }

  @Transactional
  public Boolean deleteSelectReview(String id) {
    try {
      reviewRepository.deleteById(id);
    } catch (Exception e) {
      logger.error("deleteSelectReview", e);
      return false;
    }
    return true;
  }

  public Boolean patchSelectReview(ReviewPatchDTO reviewPatchDTO, String id) {
    try {
      Review review = reviewRepository.findByIdAndStatus(id, Status.COMPLETED).get();
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

  public boolean validateTime(User user, String hashRevId) {
    Review review = null;
    LocalDateTime currentDateTime = LocalDateTime.now();
    try {
      review =
          reviewRepository.findFirstByUserAndStatusAndHashRevIDOrderByCreatedAtDesc(
              user, Status.COMPLETED, hashRevId);
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
      reviewAvgRatingGetDTO.setAvgRating(reviewRepository.getAvgRating(username, Status.COMPLETED));
      return reviewAvgRatingGetDTO;
    } catch (Exception e) {
      logger.error("getAvgRatingOfReview", e);
      return null;
    }
  }

  public boolean setStatus(String id, Status status) {
    try {
      Review review = reviewRepository.findById(id).get();
      review.setStatus(status);
      reviewRepository.save(review);
    } catch (Exception e) {
      logger.error("setStatus", e);
      return false;
    }
    return true;
  }

  public User getUser(int id) {
    User user;
    try {
      user = userRepository.findById(id).get();
    } catch (Exception e) {
      logger.error("getting error - ", e);
      return null;
    }
    return user;
  }

  public Review getReviewById(String id) {
    Review review = null;
    try {
      review = reviewRepository.findById(id).get();
    } catch (NoSuchElementException e) {
      logger.error("getReview", e);
      return null;
    }
    return review;
  }

  public List<Review> getAllReviewsByEmail(String email) {
    Optional<User> user = userRepository.findByMail(email);
    if (user.isPresent()) {
      return reviewRepository.findAllByUserAndStatus(user.get(), Status.COMPLETED);
    }
    return null;
  }
}
