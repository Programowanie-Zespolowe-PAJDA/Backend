package umk.mat.pajda.ProjektZespolowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
  // a
}
