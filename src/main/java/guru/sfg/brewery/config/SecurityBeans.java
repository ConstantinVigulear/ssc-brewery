package guru.sfg.brewery.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.ICredentialRepository;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * @author : crme059, Constantin Vigulear
 */
@Configuration
public class SecurityBeans {

  @Bean
  public GoogleAuthenticator googleAuthenticator(ICredentialRepository credentialRepository) {
    GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder configBuilder =
        new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();

    configBuilder
            .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(60))
            .setWindowSize(10)
            .setNumberOfScratchCodes(0);

    GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(configBuilder.build());
    googleAuthenticator.setCredentialRepository(credentialRepository);
    return googleAuthenticator;
  }

  // needed for use with Spring Data JPA SPeL
  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }

  // needed for generating remember me token and storing it in a database
  @Bean
  public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
  }

  // needed for catch login events
  @Bean
  public AuthenticationEventPublisher authenticationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
