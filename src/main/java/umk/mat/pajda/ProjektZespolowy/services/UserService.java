package umk.mat.pajda.ProjektZespolowy.services;

import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.UserDTO;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
public class UserService {

  private final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserConverter userConverter;
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserConverter userConverter, UserRepository userRepository) {
    this.userConverter = userConverter;
    this.userRepository = userRepository;
  }

  public boolean addUser(UserDTO userDTO) {
    try {
      userDTO.setId(null);
      userRepository.save(userConverter.createEntity(userDTO));
    } catch (Exception e) {
      logger.error("addUser", e);
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
      logger.error("getUser(int)", e);
      return null;
    }
    if (returnData == null) {
      return null;
    }

    return userConverter.createDTO(returnData);
  }

  public UserDTO getUser(String name) {
    User returnData = null;
    try {
      returnData = userRepository.findByName(name);
    } catch (NoSuchElementException e) {
      logger.error("getUser(String)", e);
      return null;
    }
    if (returnData == null) {
      return null;
    }

    return userConverter.createDTO(returnData);
  }

  public boolean deleteSelectedUser(int id) {
    try {
      userRepository.deleteById(id);
    } catch (Exception e) {
      logger.error("deleteSelectedUser", e);
      return false;
    }
    return true;
  }

  public boolean patchSelectedUser(UserDTO userDTO) {
    try {
      userRepository.save(userConverter.createEntity(userDTO));
    } catch (Exception e) {
      logger.error("patchSelectedUser", e);
      return false;
    }
    return true;
  }
}
