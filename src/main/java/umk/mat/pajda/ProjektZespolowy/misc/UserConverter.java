package umk.mat.pajda.ProjektZespolowy.misc;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Component
public class UserConverter {

  public UserDTO createDTO(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setName(user.getName());
    userDTO.setMail(user.getMail());
    userDTO.setSurname(user.getSurname());
    userDTO.setLocation(user.getLocation());
    return userDTO;
  }

  public List<UserDTO> createUserDTOList(List<User> list) {
    List<UserDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
