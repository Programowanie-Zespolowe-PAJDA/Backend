package umk.mat.pajda.ProjektZespolowy.misc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.TipGetDTO;
import umk.mat.pajda.ProjektZespolowy.DTO.TipPatchPostDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class TipConverter {

  private final UserRepository userRepository;

  @Autowired
  public TipConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public TipGetDTO createDTO(Tip tip) {
    TipGetDTO tipGetDTO = new TipGetDTO();
    tipGetDTO.setId(tip.getId());
    tipGetDTO.setCurrency(tip.getCurrency());
    tipGetDTO.setAmount(tip.getAmount());
    tipGetDTO.setPaidWith(tip.getPaidWith());
    tipGetDTO.setCreatedAt(tip.getCreatedAt());
    tipGetDTO.setUserId(tip.getUser().getId());
    return tipGetDTO;
  }

  public Tip createEntity(TipPatchPostDTO tipPatchPostDTO) {
    Tip tip = new Tip();
    tip.setCurrency(tipPatchPostDTO.getCurrency());
    tip.setAmount(tipPatchPostDTO.getAmount());
    tip.setCreatedAt(LocalDateTime.now());
    tip.setPaidWith(tipPatchPostDTO.getPaidWith());
    tip.setUser(userRepository.findById(tipPatchPostDTO.getUserId()).get());
    return tip;
  }

  public List<TipGetDTO> createTipDTOList(List<Tip> list) {
    List<TipGetDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
