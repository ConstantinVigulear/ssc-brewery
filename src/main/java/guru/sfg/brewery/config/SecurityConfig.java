package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author : crme059, Constantin Vigulear
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  // needed for use with Spring Data JPA SPeL
  @Bean
  public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
    return new SecurityEvaluationContextExtension();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            authorize -> {
              authorize
                  .antMatchers("/h2-console/**")
                  .permitAll() // do not use in production!
                  .antMatchers("/", "/webjars/**", "/login", "/resources/**")
                  .permitAll();
            })
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin(
            loginConfigurer -> {
              loginConfigurer
                  .loginProcessingUrl("/login")
                  .loginPage("/")
                  .permitAll()
                  .successForwardUrl("/")
                  .defaultSuccessUrl("/")
                      .failureUrl("/?error");
            })
        .logout(
            logoutConfigurer -> {
              logoutConfigurer
                  .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                  .logoutSuccessUrl("/?logout")
                  .permitAll();
            })
        .httpBasic()
        .and()
        .csrf()
        .ignoringAntMatchers("/h2-console/**", "/api/**");

    // h2 console config
    http.headers().frameOptions().sameOrigin();
  }

  //  @Override
  //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //
  //    auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());
  //
  ////    auth.inMemoryAuthentication()
  ////        .withUser("root")
  ////        .password("{noop}root")
  ////        .roles("ADMIN")
  ////        .and()
  ////        .withUser("user")
  ////        .password(
  ////
  // "{sha256}316e5de1c610c45833f563cb8a8ec3e5c0052e7930a39515a2be59624c007cc262870be7d8f39aa2")
  ////        .roles("USER")
  ////        .and()
  ////        .withUser("scott")
  ////        .password("{bcrypt15}$2a$15$ujKFzfxiJ72ob6/B5f1luOJnR7roXcWAR7ROFuEkZ9AWGCQqY5ldu")
  ////        .roles("CUSTOMER");
  //  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
