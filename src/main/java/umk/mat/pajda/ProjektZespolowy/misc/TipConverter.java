package umk.mat.pajda.ProjektZespolowy.misc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.TipDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class TipConverter {

  private final UserRepository userRepository;

  @Autowired
  public TipConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public TipDTO createDTO(Tip tip) {
    TipDTO tipDTO = new TipDTO();
    tipDTO.setCurrency(tip.getCurrency());
    tipDTO.setAmount(tip.getAmount());
    tipDTO.setPaidWith(tip.getPaidWith());
    tipDTO.setCreatedAt(tip.getCreatedAt());
    return tipDTO;
  }

  public Tip createEntity(TipDTO tipDTO) {
    Tip tip = new Tip();
    tip.setCurrency(tipDTO.getCurrency());
    tip.setAmount(tipDTO.getAmount());
    tip.setCreatedAt(LocalDateTime.now());
    tip.setPaidWith(tipDTO.getPaidWith());
    tip.setUser(userRepository.findById(tipDTO.getUserId()).get());
    return tip;
  }

  public List<TipDTO> createTipDTOList(List<Tip> list) {
    List<TipDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
