package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
public class UserService {
  private final UserConverter userConverter;
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserConverter userConverter, UserRepository userRepository) {
    this.userConverter = userConverter;
    this.userRepository = userRepository;
  }

  public boolean addUser(UserDTO userDTO) {
    try {
      userDTO.setId(0);
      userRepository.save(userConverter.createEntity(userDTO));
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public List<UserDTO> getAllUsers() {
    return userConverter.createUserDTOList(userRepository.findAll());
  }

  public UserDTO getUser(int id) {
    User returnData = null;
    try {
      returnData = userRepository.findById(id).get();
    } catch (NoSuchElementException e) {
      return null;
    }

    return userConverter.createDTO(returnData);
  }

  public boolean deleteSelectedUser(int id) {
    try {
      userRepository.deleteById(id);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public boolean patchSelectedUser(UserDTO userDTO) {
    try {
      userRepository.save(userConverter.createEntity(userDTO));
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
