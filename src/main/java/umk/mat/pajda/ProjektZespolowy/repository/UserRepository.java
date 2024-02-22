package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Token;
import umk.mat.pajda.ProjektZespolowy.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByMail(String mail);

  Optional<User> findByToken(Token token);

  List<User> findByEnabled(boolean enabled);

  void deleteByMail(String mail);
}
