package umk.mat.pajda.ProjektZespolowy.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import umk.mat.pajda.ProjektZespolowy.services.AuthService;
import umk.mat.pajda.ProjektZespolowy.services.JWTService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JWTService jwtService;
  private final AuthService authService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String userEmail;
    String prefix = "Bearer ";

    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, prefix)) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = authHeader.substring(prefix.length());
    userEmail = jwtService.extractUserName(jwt);

    if (StringUtils.isNotEmpty(userEmail)
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = authService.userDetailsService().loadUserByUsername(userEmail);

      if (jwtService.isTokenValid(jwt, userDetails)) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);
      }
    }
    filterChain.doFilter(request, response);
  }
}
