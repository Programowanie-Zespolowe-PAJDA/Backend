package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.User;
import umk.mat.pajda.ProjektZespolowy.misc.Status;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
  List<Review> findAllByUserAndStatus(User user, Status status);

  List<Review> findAllByStatus(Status status);

  Review findByIdAndUserAndStatus(String id, User user, Status status);

  @Query("select AVG(r.rating) from Review r where r.user.mail = :mail and r.status = :status")
  Double getAvgRating(@Param("mail") String mail, @Param("status") Status completed);

  Review findFirstByUserAndStatusAndHashRevIDOrderByCreatedAtDesc(
      User user, Status status, String hashRevID);

  void deleteById(String id);

  Optional<Review> findByIdAndStatus(String id, Status status);

  Optional<Review> findById(String id);
}
