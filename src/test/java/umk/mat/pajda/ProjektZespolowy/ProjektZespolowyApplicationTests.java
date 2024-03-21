package umk.mat.pajda.ProjektZespolowy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("tests")
@TestPropertySource(
    properties = {
      "FIXEDSALT_IPHASH = $2a$10$9elrbM0La5ooQgMP7i9yjO",
      "SHOP_ID = shop_id",
      "CLIENT_SECRET = client_secret",
      "CLIENT_ID = client_id",
      "profile = tests",
      "KEY_MD5 = key_md5"
    })
class ProjektZespolowyApplicationTests {

  @Test
  void contextLoads() {}
}
