package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.ChangePasswordGroup;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.CreatingEntityGroup;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.EditingEntityGroup;
import umk.mat.pajda.ProjektZespolowy.validatorsGroups.LoginGroup;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private Integer id;

  @Size(
      min = 2,
      max = 30,
      groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  @Pattern(
      regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*$",
      groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  private String name;

  @Size(
      min = 2,
      max = 30,
      groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  @Pattern(
      regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*+(?:[- ]?[A-ZĄĆĘŁŃÓŚŹŻ][a-zząćęłńóśźż]*)?$",
      groups = {CreatingEntityGroup.class, EditingEntityGroup.class})
  private String surname;

  @Size(
      min = 8,
      max = 30,
      groups = {LoginGroup.class, CreatingEntityGroup.class, ChangePasswordGroup.class})
  @Pattern(
      regexp = "^(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*\\d)(?=.*[!@#$%^&*]).*$",
      groups = {LoginGroup.class, CreatingEntityGroup.class, ChangePasswordGroup.class})
  private String password;

  @Size(
      min = 8,
      max = 30,
      groups = {CreatingEntityGroup.class, ChangePasswordGroup.class})
  @Pattern(
      regexp = "^(?=.*[a-ząćęłńóśźż])(?=.*[A-ZĄĆĘŁŃÓŚŹŻ])(?=.*\\d)(?=.*[!@#$%^&*]).*$",
      groups = {CreatingEntityGroup.class, ChangePasswordGroup.class})
  private String retypedPassword;

  @Email(groups = {LoginGroup.class, CreatingEntityGroup.class, EditingEntityGroup.class})
  private String mail;

  private String location;
}
