package umk.mat.pajda.ProjektZespolowy.misc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewArchiveGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewArchivePatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.ReviewArchive;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class ReviewArchiveConverter {

  private final UserRepository userRepository;

  @Autowired
  public ReviewArchiveConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ReviewArchiveGetDTO createDTO(ReviewArchive reviewArchive) {
    ReviewArchiveGetDTO reviewArchiveGetDTO = new ReviewArchiveGetDTO();
    reviewArchiveGetDTO.setCreatedAt(reviewArchive.getCreatedAt());
    reviewArchiveGetDTO.setRating(reviewArchive.getRating());
    reviewArchiveGetDTO.setComment(reviewArchive.getComment());
    reviewArchiveGetDTO.setClientName(reviewArchive.getClientName());
    reviewArchiveGetDTO.setUserID(reviewArchive.getUser().getId());
    return reviewArchiveGetDTO;
  }

  public ReviewArchive createEntity(ReviewArchivePatchPostDTO reviewArchivePatchPostDTO) {
    ReviewArchive reviewArchive = new ReviewArchive();
    reviewArchive.setRating(reviewArchivePatchPostDTO.getRating());
    reviewArchive.setComment(reviewArchivePatchPostDTO.getComment());
    reviewArchive.setCreatedAt(LocalDateTime.now());
    reviewArchive.setClientName(reviewArchivePatchPostDTO.getClientName());
    reviewArchive.setHashRevID(reviewArchivePatchPostDTO.getHashRevID());
    reviewArchive.setUser(userRepository.findById(reviewArchivePatchPostDTO.getUserID()).get());
    return reviewArchive;
  }

  public List<ReviewArchiveGetDTO> createReviewArchiveDTOList(List<ReviewArchive> list) {
    List<ReviewArchiveGetDTO> listDTO =
        list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
