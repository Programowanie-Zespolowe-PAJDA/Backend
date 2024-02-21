package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
  List<Review> findAllByUser(User user);

  Review findByIdAndUser(int id, User user);
}
