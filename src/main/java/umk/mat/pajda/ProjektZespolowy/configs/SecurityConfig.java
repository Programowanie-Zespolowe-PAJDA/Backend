package umk.mat.pajda.ProjektZespolowy.configs;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user =
        User.builder()
            .username("user")
            .password(passwordEncoder().encode("user"))
            .roles("USER")
            .build();

    UserDetails admin =
        User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            authorize ->
                authorize
                    .requestMatchers("/hello")
                    .permitAll()
                    .requestMatchers("/admin")
                    .hasRole("ADMIN")
                    .requestMatchers("/authenticated")
                    .authenticated())
        .formLogin(withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
