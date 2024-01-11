package umk.mat.pajda.ProjektZespolowy.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.TipDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class TipConverter {

    private final UserConverter userConverter;

    @Autowired
    public TipConverter(UserConverter userConverter){
        this.userConverter = userConverter;
        userConverter.setTipConverter(this);
    }

    public TipDTO createDTO(Tip tip) {
        TipDTO tipDTO = new TipDTO();

        tipDTO.setId(tip.getId());

        tipDTO.setCurrency(tip.getCurrency());
        tipDTO.setAmount(tip.getAmount());

        tipDTO.setPaymentTime(tip.getPaymentTime());
        tipDTO.setPaidWith(tip.getPaidWith());

        tipDTO.setUser(userConverter.createDTO(tip.getUser()));

        return tipDTO;
    }

    public Tip createEntity(TipDTO tipDTO) {
        Tip tip = new Tip();

        tip.setId(tipDTO.getId());

        tip.setCurrency(tipDTO.getCurrency());
        tip.setAmount(tipDTO.getAmount());

        tip.setPaymentTime(tipDTO.getPaymentTime());
        tip.setPaidWith(tipDTO.getPaidWith());

        tip.setUser(userConverter.createEntity(tipDTO.getUser()));

        return tip;
    }

    public List<TipDTO> createTipDTOList(List<Tip> list) {
        List<TipDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
        return listDTO;
    }
}
