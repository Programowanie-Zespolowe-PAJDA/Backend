package umk.mat.pajda.ProjektZespolowy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"ADMIN_PASSWORD=test_admin", "USER_PASSWORD=test_user"})
class ProjektZespolowyApplicationTests {

  @Test
  void contextLoads() {}
}
