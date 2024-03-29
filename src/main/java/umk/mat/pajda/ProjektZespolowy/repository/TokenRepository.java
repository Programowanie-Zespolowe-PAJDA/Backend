package umk.mat.pajda.ProjektZespolowy.repository;

import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umk.mat.pajda.ProjektZespolowy.entity.Token;

@Repository
@Profile("prod")
public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByToken(String token);
}
