package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;

@Service
public class ReviewService {

  private final Logger logger = LoggerFactory.getLogger(ReviewService.class);
  private final ReviewRepository reviewRepository;
  private final ReviewConverter reviewConverter;

  @Autowired
  public ReviewService(ReviewRepository reviewRepository, ReviewConverter reviewConverter) {
    this.reviewRepository = reviewRepository;
    this.reviewConverter = reviewConverter;
  }

  public Boolean addReview(ReviewDTO reviewDTO) {
    try {
      reviewDTO.setId(0);
      reviewRepository.save(reviewConverter.createEntity(reviewDTO));
    } catch (Exception e) {
      logger.error("addReview", e);
      return false;
    }
    return true;
  }

  public List<ReviewDTO> getAllReviews() {
    return reviewConverter.createReviewDTOList(reviewRepository.findAll());
  }

  public ReviewDTO getReview(int id) {
    Review returnData = null;
    try {
      returnData = reviewRepository.findById(id).get();
    } catch (NoSuchElementException e) {
      logger.error("getReview", e);
      return null;
    }

    return reviewConverter.createDTO(returnData);
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

  public Boolean patchSelectReview(ReviewDTO reviewDTO) {
    try {
      reviewRepository.save(reviewConverter.createEntity(reviewDTO));
    } catch (Exception e) {
      logger.error("patchSelectReview", e);
      return false;
    }
    return true;
  }
}
