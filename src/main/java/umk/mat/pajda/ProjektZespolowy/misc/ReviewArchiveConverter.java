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

  public ReviewArchiveDTO createDTO(ReviewArchive source) {
    ReviewArchiveDTO res = new ReviewArchiveDTO();
    res.setId(source.getId());
    res.setRating(source.getRating());
    res.setComment(source.getComment());
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    res.setReviewTimeStamp(source.getReviewTimeStamp());
    res.setUser(userConverter.createDTO(source.getUser()));
    res.setClientName(source.getClientName());
    res.setHashRevID(source.getHashRevID());
    return res;
  }

  public ReviewArchive createEntity(ReviewArchiveDTO source) {
    ReviewArchive res = new ReviewArchive();
    res.setId(source.getId());
    res.setRating(source.getRating());
    res.setComment(source.getComment());
    res.setReviewTimeStamp(source.getReviewTimeStamp());
    res.setUser(userConverter.createEntity(source.getUser()));
    res.setClientName(source.getClientName());
    res.setHashRevID(source.getHashRevID());
    return res;
  }

  public List<ReviewArchiveDTO> createReviewArchiveDTOList(List<ReviewArchive> list) {
    List<ReviewArchiveDTO> listDTO =
        list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
