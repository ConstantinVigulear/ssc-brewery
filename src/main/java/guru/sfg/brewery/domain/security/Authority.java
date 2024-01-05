package guru.sfg.brewery.domain.security;

import java.util.Set;
import javax.persistence.*;
import lombok.*;

/**
 * @author : crme059, Constantin Vigulear
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Authority {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String permission;

  @ManyToMany(mappedBy = "authorities")
  private Set<Role> roles;
}
