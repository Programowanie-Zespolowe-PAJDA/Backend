package umk.mat.pajda.ProjektZespolowy.misc;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Component
public class UserConverter {

  private ReviewConverter reviewConverter;
  private TipConverter tipConverter;

  public UserConverter() {}

  public UserDTO createDTO(User user) {
    UserDTO userDTO = new UserDTO();

    userDTO.setId(user.getId());

    userDTO.setName(user.getName());
    userDTO.setMail(user.getMail());

    userDTO.setSurname(user.getSurname());
    userDTO.setPassword(user.getPassword());

    userDTO.setLocation(user.getLocation());

    userDTO.setTipDTOList(tipConverter.createTipDTOList(user.getTipList()));
    userDTO.setReviewDTOList(reviewConverter.createReviewDTOList(user.getReviewList()));

    return userDTO;
  }

  public User createEntity(UserDTO userDTO) {
    User user = new User();

    user.setId(userDTO.getId());

    user.setName(userDTO.getName());
    user.setMail(userDTO.getMail());

    user.setSurname(userDTO.getSurname());
    user.setPassword(userDTO.getPassword());

    user.setLocation(userDTO.getLocation());

    user.setTipList(userDTO.getTipDTOList().stream().map(tipConverter::createEntity).toList());
    user.setReviewList(
        userDTO.getReviewDTOList().stream().map(reviewConverter::createEntity).toList());

    return user;
  }

  public List<UserDTO> createUserDTOList(List<User> list) {
    List<UserDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }

  public ReviewConverter getReviewConverter() {
    return reviewConverter;
  }

  public void setReviewConverter(ReviewConverter reviewConverter) {
    this.reviewConverter = reviewConverter;
  }

  public TipConverter getTipConverter() {
    return tipConverter;
  }

  public void setTipConverter(TipConverter tipConverter) {
    this.tipConverter = tipConverter;
  }
}
