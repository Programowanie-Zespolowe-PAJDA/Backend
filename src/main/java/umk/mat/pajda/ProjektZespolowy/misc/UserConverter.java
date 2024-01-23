package umk.mat.pajda.ProjektZespolowy.misc;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Component
public class UserConverter {

  @Autowired private PasswordEncoder passwordEncoder;

  public UserDTO createDTO(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setName(user.getName());
    userDTO.setMail(user.getMail());
    userDTO.setSurname(user.getSurname());
    userDTO.setLocation(user.getLocation());
    return userDTO;
  }

  public User createEntity(UserDTO userDTO) {
    User user = new User();
    user.setName(userDTO.getName());
    user.setMail(userDTO.getMail());
    user.setSurname(userDTO.getSurname());
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    user.setLocation(userDTO.getLocation());
    return user;
  }

  public List<UserDTO> createUserDTOList(List<User> list) {
    List<UserDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
