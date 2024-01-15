package org.mcq.spring6restmvc.repository;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mcq.spring6restmvc.DataLoader;
import org.mcq.spring6restmvc.entities.Beer;
import org.mcq.spring6restmvc.model.BeerStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .name("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("348375")
                .price(new BigDecimal("11.99"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder()
                    .name("My Beeeeeeeeeeeerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr lol")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("348375")
                    .price(new BigDecimal("11.99"))
                    .build());

            beerRepository.flush();
        });
    }
}
