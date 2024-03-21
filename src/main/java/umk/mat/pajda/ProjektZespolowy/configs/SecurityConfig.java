package umk.mat.pajda.ProjektZespolowy.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import umk.mat.pajda.ProjektZespolowy.services.AuthService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthService authService;

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(authService.userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        HttpMethod.PATCH,
                        "/user/editInformations",
                        "/user/editPassword",
                        "/user/editEmail",
                        "/user/editBankAccountNumber")
                    .authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/user")
                    .authenticated()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/authenticated",
                        "/review/owner",
                        "/review/owner/{id}",
                        "/tip/stats",
                        "review/avgRating",
                        "/user/profile")
                    .authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/review/{id}")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/user", "/admin")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/review/{id}", "/user/{id}")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .permitAll())
        .sessionManagement(
            manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }
}
