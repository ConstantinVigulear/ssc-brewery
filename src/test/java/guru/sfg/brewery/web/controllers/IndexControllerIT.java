package guru.sfg.brewery.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerOrderService;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author : crme059, Constantin Vigulear
 */
@SpringBootTest
public class IndexControllerIT extends BaseIT {

  @MockBean BeerRepository beerRepository;
  @MockBean BeerInventoryRepository beerInventoryRepository;
  @MockBean BreweryService breweryService;
  @MockBean CustomerRepository customerRepository;
  @MockBean BeerService beerService;
  @MockBean BeerOrderService beerOrderService;

  @Test
  void testGetIndexSlash() throws Exception {
    mockMvc.perform(get("/")).andExpect(status().isOk());
  }
}
