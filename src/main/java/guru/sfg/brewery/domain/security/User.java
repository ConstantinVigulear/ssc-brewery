package guru.sfg.brewery.domain.security;

import guru.sfg.brewery.domain.Customer;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author : crme059, Constantin Vigulear
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User implements UserDetails, CredentialsContainer {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String username;
  private String password;

  @Singular
  @ManyToMany(
      cascade = {CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
      inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
  private Set<Role> roles;

  @ManyToOne(fetch = FetchType.EAGER)
  private Customer customer;

  @Transient
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
        .map(Role::getAuthorities)
        .flatMap(Set::stream)
        .map(authority -> new SimpleGrantedAuthority(authority.getPermission()))
        .collect(Collectors.toSet());
  }

  @Builder.Default private boolean accountNonExpired = true;

  @Builder.Default private boolean accountNonLocked = true;

  @Builder.Default private boolean credentialsNonExpired = true;

  @Builder.Default private boolean enabled = true;

  @Builder.Default private Boolean useGoogle2fa = false;

  private String google2faSecret;

  @Transient
  private Boolean google2faRequired = true;

  @CreationTimestamp
  @Column(updatable = false)
  private Timestamp createdDate;

  @UpdateTimestamp private Timestamp lastModifiedDate;

  @Override
  public void eraseCredentials() {
    this.password = null;
  }
}
