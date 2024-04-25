package umk.mat.pajda.ProjektZespolowy.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RatingDTO {
  private Integer rating;
  private Integer count;

  public RatingDTO(Integer rating, Long count) {
    this.rating = rating;
    this.count = Math.toIntExact(count);
  }
}
