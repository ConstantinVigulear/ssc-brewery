package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * @author : crme059, Constantin Vigulear
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener {

  private final UserRepository userRepository;
  private final LoginFailureRepository loginFailureRepository;

  @EventListener
  public void listen(AuthenticationFailureBadCredentialsEvent event) {
    log.debug("Login Failure");

    if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
      LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

      UsernamePasswordAuthenticationToken token =
          (UsernamePasswordAuthenticationToken) event.getSource();

      if (token.getPrincipal() instanceof String) {
        String username = token.getPrincipal().toString();
        builder.userName(username);
        userRepository.findByUsername(username).ifPresent(builder::user);

        log.debug("Attempted Username: " + username);
      }

      if (token.getDetails() instanceof WebAuthenticationDetails) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
        builder.sourceIp(details.getRemoteAddress());

        log.debug("Source IP: " + details.getRemoteAddress());
      }

      LoginFailure loginFailure = loginFailureRepository.save(builder.build());
      log.debug("Login Failure saved. Id: " + loginFailure.getId());

      if (loginFailure.getUser() != null) {
        lockUserAccount(loginFailure.getUser());
      }
    }
  }

  private void lockUserAccount(User user) {
    List<LoginFailure> failures =
        loginFailureRepository.findAllByUserAndCreatedDateIsAfter(
            user, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

    if (failures.size() > 3) {
      log.debug("Locking User Account... ");
      user.setAccountNonLocked(false);
      userRepository.save(user);
    }
  }
}
