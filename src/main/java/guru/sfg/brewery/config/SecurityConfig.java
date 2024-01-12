package guru.sfg.brewery.config;

import guru.sfg.brewery.security.google.Google2faFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author : crme059, Constantin Vigulear
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final PersistentTokenRepository persistentTokenRepository;
  private final Google2faFilter google2faFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.addFilterBefore(google2faFilter, SessionManagementFilter.class);

    http.cors()
        .and()
        .authorizeRequests(
            authorize ->
                authorize
                    .antMatchers("/h2-console/**")
                    .permitAll() // do not use in production!
                    .antMatchers("/", "/webjars/**", "/login", "/resources/**")
                    .permitAll())
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin(
            loginConfigurer ->
                loginConfigurer
                    .loginProcessingUrl("/login")
                    .loginPage("/")
                    .permitAll()
                    .successForwardUrl("/")
                    .defaultSuccessUrl("/")
                    .failureUrl("/?error"))
        .logout(
            logoutConfigurer ->
                logoutConfigurer
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessUrl("/?logout")
                    .permitAll())
        .httpBasic()
        .and()
        .csrf()
        .ignoringAntMatchers("/h2-console/**", "/api/**")
        .and()
        .rememberMe()
        .tokenRepository(persistentTokenRepository)
        .userDetailsService(userDetailsService);

    // .rememberMe()
    // .key("sfg-key")
    // .userDetailsService(userDetailsService);

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
}
