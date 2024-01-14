package org.mcq.spring6restmvc.repository;

import org.junit.jupiter.api.Test;
import org.mcq.spring6restmvc.DataLoader;
import org.mcq.spring6restmvc.entities.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder().name("My Beer").build());
        assertThat(savedBeer.getId()).isNotNull();
    }
}
