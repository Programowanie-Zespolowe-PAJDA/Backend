package umk.mat.pajda.ProjektZespolowy.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewArchiveDTO{

    @NotNull
    private Integer id;

    @Size(min=0, max=10)
    private Integer rating;

    @Size(max=300)
    private String comment;

    @PastOrPresent
    private LocalDateTime reviewTimeStamp;

    @Size(max=30)
    private String clientName;

    @NotNull
    private String hashRevID;

    @NotNull
    private UserDTO user;
}
