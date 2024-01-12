package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author : crme059, Constantin Vigulear
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserUnlockService {
  private final UserRepository userRepository;

  @Scheduled(fixedRate = 300000)
  public void unlockAccount() {
    log.debug("Running Unlock Accounts");

    List<User> lockedUsers =
        userRepository.findAllByAccountNonLockedAndLastModifiedDateIsBefore(
            false, Timestamp.valueOf(LocalDateTime.now().minusSeconds(30)));

    if (!lockedUsers.isEmpty()) {
      log.debug("Locked Accounts Found, Unlocking");
      lockedUsers.forEach(user -> user.setAccountNonLocked(true));

      userRepository.saveAll(lockedUsers);
    }
  }
}
