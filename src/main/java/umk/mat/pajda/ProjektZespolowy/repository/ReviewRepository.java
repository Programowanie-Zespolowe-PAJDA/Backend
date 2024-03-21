package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query("select AVG(r.rating) from Review r where r.user.mail = :mail")
  Double getAvgRating(@Param("mail") String mail);
}
