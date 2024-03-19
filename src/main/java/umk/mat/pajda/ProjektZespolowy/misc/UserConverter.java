package umk.mat.pajda.ProjektZespolowy.misc;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.*;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class UserConverter {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Value("${profile}")
  private String profile;

  public UserConverter(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public UserGetDTO createDTO(User user) {
    UserGetDTO userGetDTO = new UserGetDTO();
    userGetDTO.setId(user.getId());
    userGetDTO.setName(user.getName());
    userGetDTO.setMail(user.getMail());
    userGetDTO.setSurname(user.getSurname());
    userGetDTO.setLocation(user.getLocation());
    userGetDTO.setBankAccountNumber(user.getBankAccountNumber());
    return userGetDTO;
  }

  public User createEntity(RegisterDTO registerDTO) {
    User user = new User();
    user.setMail(registerDTO.getMail());
    user.setName(registerDTO.getName());
    user.setSurname(registerDTO.getSurname());
    user.setLocation(registerDTO.getLocation());
    user.setBankAccountNumber(registerDTO.getBankAccountNumber());
    user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
    user.setRole("ROLE_USER");
    if ("prod".equals(profile)) {
      user.setEnabled(false);
    } else {
      user.setEnabled(true);
    }
    return user;
  }

  public User updateInformationsOfEntity(
      UserPatchInformationsDTO userPatchInformationsDTO, String mail) {
    User user = userRepository.findByMail(mail).get();
    user.setName(userPatchInformationsDTO.getName());
    user.setSurname(userPatchInformationsDTO.getSurname());
    user.setLocation(userPatchInformationsDTO.getLocation());
    return user;
  }

  public User updatePasswordOfEntity(UserPatchPasswordDTO userPatchPasswordDTO, String mail) {
    User user = userRepository.findByMail(mail).get();
    user.setPassword(passwordEncoder.encode(userPatchPasswordDTO.getPassword()));
    return user;
  }

  public User updateEmailOfEntity(UserPatchEmailDTO userPatchEmailDTO, String mail) {
    User user = userRepository.findByMail(mail).get();
    user.setMail(userPatchEmailDTO.getMail());
    return user;
  }

  public User updateBankAccountNumberOfEntity(
      UserPatchBankAccountNumberDTO userPatchBankAccountNumberDTO, String mail) {
    User user = userRepository.findByMail(mail).get();
    user.setBankAccountNumber(userPatchBankAccountNumberDTO.getBankAccountNumber());
    return user;
  }

  public List<UserGetDTO> createUserDTOList(List<User> list) {
    List<UserGetDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
