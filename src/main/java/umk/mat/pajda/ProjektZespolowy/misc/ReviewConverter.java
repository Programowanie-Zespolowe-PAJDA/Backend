package umk.mat.pajda.ProjektZespolowy.misc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;

@Component
public class ReviewConverter {

    private final UserConverter userConverter;

    @Autowired
    public ReviewConverter(UserConverter userConverter){
        this.userConverter = userConverter;
        userConverter.setReviewConverter(this);
    }

    public ReviewDTO createDTO(Review source) {
        ReviewDTO res = new ReviewDTO();
        res.setId(source.getId());
        res.setRating(source.getRating());
        res.setComment(source.getComment());

        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        res.setReviewTimeStamp(source.getReviewTimeStamp());
        res.setUser(userConverter.createDTO(source.getUser()));
        res.setClientName(source.getClientName());
        res.setHashRevID(source.getHashRevID());

        return res;
    }

    public Review createEntity(ReviewDTO source) {
        Review res = new Review();
        res.setId(source.getId());
        res.setRating(source.getRating());
        res.setComment(source.getComment());
        res.setReviewTimeStamp(source.getReviewTimeStamp());
        res.setUser(userConverter.createEntity(source.getUser()));
        res.setClientName(source.getClientName());
        res.setHashRevID(source.getHashRevID());
        return res;
    }

    public List<ReviewDTO> createReviewDTOList(List<Review> list) {
        List<ReviewDTO> listDTO = list.stream().map(this::createDTO).collect(Collectors.toList());
        return listDTO;
    }
}
