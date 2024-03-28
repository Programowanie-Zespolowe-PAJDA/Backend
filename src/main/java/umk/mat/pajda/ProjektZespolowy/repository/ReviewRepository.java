package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
  List<Review> findAllByUserAndEnabledIsTrue(User user);

  List<Review> findAllByEnabledIsTrue();

  Review findByIdAndUserAndEnabledIsTrue(String id, User user);

  Review findFirstByUserAndEnabledIsTrueAndHashRevIDOrderByCreatedAtDesc(
      User user, String hashRevID);

  void deleteById(String id);

  Optional<Review> findById(String id);
}
