package umk.mat.pajda.ProjektZespolowy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  User findByName(String name);
}
