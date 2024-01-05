package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : crme059, Constantin Vigulear
 */
public class PasswordEncodingTest {

  static final String PASSWORD = "password";

  @Test
  void testNoOp() {
    PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
    System.out.println(noOp.encode(PASSWORD));
  }

  @Test
  void testLdab() {
    PasswordEncoder ldap = new LdapShaPasswordEncoder();
    System.out.println(ldap.encode(PASSWORD));
    System.out.println(ldap.encode(PASSWORD));

    String encodedPws = ldap.encode(PASSWORD);

    assertThat(ldap.matches(PASSWORD, encodedPws)).isTrue();
  }

  @Test
  void testSha256() {
    PasswordEncoder sha256 = new StandardPasswordEncoder();

    System.out.println(sha256.encode(PASSWORD));
    System.out.println(sha256.encode(PASSWORD));
  }

  @Test
  void testBcrypt() {
    PasswordEncoder bcrypt = new BCryptPasswordEncoder();

    System.out.println(bcrypt.encode(PASSWORD));
  }

  @Test
  void testBcrypt15() {
    PasswordEncoder bcrypt = new BCryptPasswordEncoder(15);

    System.out.println(bcrypt.encode(PASSWORD));
    System.out.println(bcrypt.encode(PASSWORD));
    System.out.println(bcrypt.encode("tiger"));
  }

  @Test
  void hashingExample() {
    System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

    String salted = PASSWORD + "ThisIsMySaltValue";
    System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
  }
}
