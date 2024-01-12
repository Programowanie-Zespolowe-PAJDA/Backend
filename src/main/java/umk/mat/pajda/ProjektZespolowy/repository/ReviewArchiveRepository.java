package umk.mat.pajda.ProjektZespolowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.ReviewArchive;

@Repository
public interface ReviewArchiveRepository extends JpaRepository<ReviewArchive, Integer> {}
