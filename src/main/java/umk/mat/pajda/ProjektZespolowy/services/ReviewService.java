package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
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
    reviewRepository.save(reviewConverter.createEntity(reviewDTO));
    return true;
  }

  // TODO - make it so it restricted to one kellner
  public List<ReviewDTO> getAllReviews() {
    return reviewConverter.createDTO(reviewRepository.findAll());
  }

  public ReviewDTO getReview(int id) {
    return reviewConverter.createDTO(reviewRepository.findById((long) id).get());
  }

  public Boolean deleteSelectReview(int id) {
    try {
      reviewRepository.deleteById((long) id);
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }

  public Boolean patchSelectReview(ReviewDTO reviewDTO) {
    try {
      reviewRepository.save(reviewConverter.createEntity(reviewDTO));
    } catch (IndexOutOfBoundsException e) {
      return false;
    }
    return true;
  }
}
