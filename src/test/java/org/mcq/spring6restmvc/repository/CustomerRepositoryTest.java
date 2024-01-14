package org.mcq.spring6restmvc.repository;

import org.junit.jupiter.api.Test;
import org.mcq.spring6restmvc.DataLoader;
import org.mcq.spring6restmvc.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer customer = customerRepository.save(Customer.builder().name("New Name").build());
        assertThat(customer.getId()).isNotNull();
    }
}
