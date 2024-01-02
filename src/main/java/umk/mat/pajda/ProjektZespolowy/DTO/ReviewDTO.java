package umk.mat.pajda.ProjektZespolowy.DTO;


import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {


    private Integer id;

    private Integer rating;

    private String comment;

    private LocalDateTime reviewTimeStamp;

    private String clientName;

    private String hashRevID;

    private Integer kellnerID;
}
