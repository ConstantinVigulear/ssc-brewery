package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : crme059, Constantin Vigulear
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
        "hasAuthority('order.read') "
        + "OR hasAuthority('customer.order.read') "
        + "AND @beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
public @interface BeerOrderReadPermission {}
