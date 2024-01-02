package umk.mat.pajda.ProjektZespolowy.misc;

import org.springframework.stereotype.Component;
import umk.mat.pajda.ProjektZespolowy.DTO.ReviewDTO;
import umk.mat.pajda.ProjektZespolowy.entity.Review;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewConverter {

    public ReviewDTO createDTO(Review source){
        ReviewDTO res = new ReviewDTO();
        res.setId(source.getId());
        res.setRating(source.getRating());
        res.setComment(source.getComment());
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        res.setReviewTimeStamp(source.getReviewTimeStamp());
        res.setKellnerID(source.getKellnerID());
        res.setClientName(source.getClientName());
        res.setHashRevID(source.getHashRevID());
        return res;
    }


    public Review createEntity(ReviewDTO source){
        Review res = new Review();
        res.setId(source.getId());
        res.setRating(source.getRating());
        res.setComment(source.getComment());
        res.setReviewTimeStamp(source.getReviewTimeStamp());
        res.setKellnerID(source.getKellnerID());
        res.setClientName(source.getClientName());
        res.setHashRevID(source.getHashRevID());
        return res;
    }

    public List<ReviewDTO> createDTO(List<Review> list) {
        List<ReviewDTO> listDTO = list.stream()
                .map(this::createDTO).collect(Collectors.toList());
        return listDTO;
    }


}
