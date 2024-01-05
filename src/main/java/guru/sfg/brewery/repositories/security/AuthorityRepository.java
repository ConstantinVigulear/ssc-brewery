package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : crme059, Constantin Vigulear
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {}
