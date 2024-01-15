package umk.mat.pajda.ProjektZespolowy.misc;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewArchiveDTO;
import umk.mat.pajda.ProjektZespolowy.entity.ReviewArchive;

@Component
public class ReviewArchiveConverter {
  private final UserConverter userConverter;

  @Autowired
  public ReviewArchiveConverter(UserConverter userConverter) {
    this.userConverter = userConverter;
  }

  public ReviewArchiveDTO createDTO(ReviewArchive reviewArchive) {
    ReviewArchiveDTO reviewArchiveDTO = new ReviewArchiveDTO();
    reviewArchiveDTO.setId(reviewArchive.getId());
    reviewArchiveDTO.setRating(reviewArchive.getRating());
    reviewArchiveDTO.setComment(reviewArchive.getComment());

    reviewArchiveDTO.setReviewTimeStamp(reviewArchive.getReviewTimeStamp());
    reviewArchiveDTO.setUser(userConverter.createDTO(reviewArchive.getUser()));
    reviewArchiveDTO.setClientName(reviewArchive.getClientName());
    reviewArchiveDTO.setHashRevID(reviewArchive.getHashRevID());
    return reviewArchiveDTO;
  }

  public ReviewArchive createEntity(ReviewArchiveDTO reviewArchiveDTO) {
    ReviewArchive reviewArchive = new ReviewArchive();
    reviewArchive.setId(reviewArchiveDTO.getId());
    reviewArchive.setRating(reviewArchiveDTO.getRating());
    reviewArchive.setComment(reviewArchiveDTO.getComment());
    reviewArchive.setReviewTimeStamp(reviewArchiveDTO.getReviewTimeStamp());
    reviewArchive.setUser(userConverter.createEntity(reviewArchiveDTO.getUser()));
    reviewArchive.setClientName(reviewArchiveDTO.getClientName());
    reviewArchive.setHashRevID(reviewArchiveDTO.getHashRevID());
    return reviewArchive;
  }

  public List<ReviewArchiveDTO> createReviewArchiveDTOList(List<ReviewArchive> list) {
    List<ReviewArchiveDTO> listDTO =
        list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
