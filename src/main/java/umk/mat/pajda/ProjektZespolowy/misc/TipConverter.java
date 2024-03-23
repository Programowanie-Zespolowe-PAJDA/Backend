package umk.mat.pajda.ProjektZespolowy.misc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.TipGetDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;
import umk.mat.pajda.ProjektZespolowy.repository.ReviewRepository;
import umk.mat.pajda.ProjektZespolowy.repository.UserRepository;

@Component
public class TipConverter {

  private final UserRepository userRepository;

  private final ReviewRepository reviewRepository;

  @Autowired
  public TipConverter(UserRepository userRepository, ReviewRepository reviewRepository) {
    this.userRepository = userRepository;
    this.reviewRepository = reviewRepository;
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

  public Tip createEntity(
      String payoutId, String orderId, String realAmount, String paidWith, String currency) {
    Tip tip = new Tip();
    tip.setId(payoutId);
    tip.setUser(reviewRepository.findById(orderId).get().getUser());
    tip.setCurrency(currency);
    tip.setCreatedAt(LocalDateTime.now());
    tip.setPaidWith(paidWith);
    tip.setAmount(Integer.valueOf(realAmount));
    return tip;
  }

  public List<TipGetDTO> createTipDTOList(List<Tip> list) {
    List<TipGetDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
    return listDTO;
  }
}
