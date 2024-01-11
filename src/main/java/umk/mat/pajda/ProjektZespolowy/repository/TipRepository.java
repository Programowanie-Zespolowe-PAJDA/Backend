package umk.mat.pajda.ProjektZespolowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Review;
import umk.mat.pajda.ProjektZespolowy.entity.Tip;

@Repository
public interface TipRepository extends JpaRepository<Tip, Integer> {
}
