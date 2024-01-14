package org.mcq.spring6restmvc;

import lombok.RequiredArgsConstructor;
import org.mcq.spring6restmvc.entities.Beer;
import org.mcq.spring6restmvc.entities.Customer;
import org.mcq.spring6restmvc.model.BeerStyle;
import org.mcq.spring6restmvc.repository.BeerRepository;
import org.mcq.spring6restmvc.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        initBeerData();
        initCustomerData();
    }

    private void initBeerData() {
        Beer beer1 = Beer.builder()
                .name("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .name("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .name("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);
    }

    private void initCustomerData() {
        Customer customer1 = Customer.builder()
                .name("Michael")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .name("Jane")
                .createdDate(LocalDateTime.now().minusYears(2))
                .updatedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .name("Doe")
                .createdDate(LocalDateTime.now().minusMonths(6))
                .updatedDate(LocalDateTime.now())
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
    }
}
