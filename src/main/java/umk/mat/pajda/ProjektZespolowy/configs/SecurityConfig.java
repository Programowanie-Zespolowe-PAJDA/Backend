package umk.mat.pajda.ProjektZespolowy.configs;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import umk.mat.pajda.ProjektZespolowy.services.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;
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
        .formLogin(withDefaults())
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  // TODO - fix spring security for review endpoints remove Temporary solution
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/review/**", "/user/**");
  }
  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}
