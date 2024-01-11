package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotNull
    @Positive
    private Integer id;

    @Size(max=30)
    private String name;

    @Size(max=30)
    private String surname;

    @Size(max=30)
    private String password;

    @Email
    private String mail;

    private String location;

    private List<TipDTO> tipDTOList;

    private List<ReviewDTO> reviewDTOList;
}