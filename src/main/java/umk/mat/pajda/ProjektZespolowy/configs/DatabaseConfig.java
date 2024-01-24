package umk.mat.pajda.ProjektZespolowy.configs;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!tests")
public class DatabaseConfig {
  @Value("${DATABASE_USERNAME}")
  private String databaseUsername;

  @Value("${DATABASE_PASSWORD}")
  private String databasePassword;

  @Value("${DATABASE_URL}")
  private String databaseUrl;

  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName("org.postgresql.Driver");
    dataSourceBuilder.url(databaseUrl);
    dataSourceBuilder.username(databaseUsername);
    dataSourceBuilder.password(databasePassword);
    return dataSourceBuilder.build();
  }
}
