package umk.mat.pajda.ProjektZespolowy.misc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewArchiveDTO;
import umk.mat.pajda.ProjektZespolowy.entity.ReviewArchive;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class ReviewArchiveConverter {

  private final UserRepository userRepository;

  @Autowired
  public ReviewArchiveConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ReviewArchiveDTO createDTO(ReviewArchive reviewArchive) {
    ReviewArchiveDTO reviewArchiveDTO = new ReviewArchiveDTO();
    reviewArchiveDTO.setCreatedAt(reviewArchive.getCreatedAt());
    reviewArchiveDTO.setRating(reviewArchive.getRating());
    reviewArchiveDTO.setComment(reviewArchive.getComment());
    reviewArchiveDTO.setClientName(reviewArchive.getClientName());
    return reviewArchiveDTO;
  }

  public ReviewArchive createEntity(ReviewArchiveDTO reviewArchiveDTO) {
    ReviewArchive reviewArchive = new ReviewArchive();
    reviewArchive.setRating(reviewArchiveDTO.getRating());
    reviewArchive.setComment(reviewArchiveDTO.getComment());
    reviewArchive.setCreatedAt(LocalDateTime.now());
    reviewArchive.setClientName(reviewArchiveDTO.getClientName());
    reviewArchive.setHashRevID(reviewArchiveDTO.getHashRevID());
    reviewArchive.setUser(userRepository.findById(reviewArchiveDTO.getUserID()).get());
    return reviewArchive;
  }

  public List<ReviewArchiveDTO> createReviewArchiveDTOList(List<ReviewArchive> list) {
    List<ReviewArchiveDTO> listDTO =
        list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
