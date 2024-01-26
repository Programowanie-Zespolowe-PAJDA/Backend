package umk.mat.pajda.ProjektZespolowy.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.UserConverter;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Service
public class UserService {

  private final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserConverter userConverter;
  private final UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserConverter userConverter, UserRepository userRepository) {
    this.userConverter = userConverter;
    this.userRepository = userRepository;
  }

  public List<UserGetDTO> getAllUsers() {
    return userConverter.createUserDTOList(userRepository.findAll());
  }

  public UserGetDTO getUser(int id) {
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

  public UserGetDTO getUser(String mail) {
    User returnData = null;
    try {
      returnData = userRepository.findByMail(mail).get();
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

  @Transactional
  public boolean deleteSelectedUser(String email) {
    try {
      userRepository.deleteByMail(email);
    } catch (Exception e) {
      logger.error("deleteSelectedUser", e);
      return false;
    }
    return true;
  }

  public boolean patchInformationsOfUser(
      UserPatchInformationsDTO userPatchInformationsDTO, String email) {
    try {
      userRepository.save(
          userConverter.updateInformationsOfEntity(userPatchInformationsDTO, email));
    } catch (Exception e) {
      logger.error("patchInformationsOfUser", e);
      return false;
    }
    return true;
  }

  public boolean patchPasswordOfUser(UserPatchPasswordDTO userPatchPasswordDTO, String email) {
    try {
      userRepository.save(userConverter.updatePasswordOfEntity(userPatchPasswordDTO, email));
    } catch (Exception e) {
      logger.error("patchPasswordOfUser", e);
      return false;
    }
    return true;
  }

  public boolean patchEmailOfUser(UserPatchEmailDTO userPatchEmailDTO, String email) {
    try {
      userRepository.save(userConverter.updateEmailOfEntity(userPatchEmailDTO, email));
    } catch (Exception e) {
      logger.error("patchEmailOfUser", e);
      return false;
    }
    return true;
  }
}
