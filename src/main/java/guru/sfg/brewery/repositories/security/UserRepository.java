package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.User;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : crme059, Constantin Vigulear
 */
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUsername(String userName);

  List<User> findAllByAccountNonLockedAndLastModifiedDateIsBefore(Boolean locked, Timestamp timestamp);
}
