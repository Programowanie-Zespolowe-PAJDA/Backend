package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
  List<Review> findAllByUserAndStatus(User user, String status);

  List<Review> findAllByStatus(String status);

  Review findByIdAndUserAndStatus(String id, User user, String status);

  Review findFirstByUserAndStatusAndHashRevIDOrderByCreatedAtDesc(
      User user, String status, String hashRevID);

  void deleteById(String id);

  Optional<Review> findByIdAndStatus(String id, String status);

  Optional<Review> findById(String id);
}
