package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.misc.ReviewConverter;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReviewConverter reviewConverter;

  @Autowired
  public ReviewService(ReviewRepository reviewRepository, ReviewConverter reviewConverter) {
    this.reviewRepository = reviewRepository;
    this.reviewConverter = reviewConverter;
  }

  public Boolean addReview(ReviewDTO reviewDTO) {
    try {
      reviewRepository.save(reviewConverter.createEntity(reviewDTO));
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public List<ReviewDTO> getAllReviews() {
    return reviewConverter.createReviewDTOList(reviewRepository.findAll());
  }

  public ReviewDTO getReview(int id) {
    Review review = null;
    try {
      review = reviewRepository.findById(id).get();
    } catch (NoSuchElementException e) {
      return null;
    }

    return reviewConverter.createDTO(review);
  }

  public Boolean deleteSelectReview(int id) {
    try {
      reviewRepository.deleteById(id);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public Boolean patchSelectReview(ReviewDTO reviewDTO) {
    try {
      Review review = reviewRepository.findById(reviewDTO.getId()).get();
      review.setClientName(reviewDTO.getClientName());
      review.setComment(reviewDTO.getComment());
      review.setRating(reviewDTO.getRating());
      reviewRepository.save(review);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
