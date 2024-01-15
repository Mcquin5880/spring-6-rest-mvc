package org.mcq.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.mcq.spring6restmvc.entities.Beer;
import org.mcq.spring6restmvc.entities.Customer;
import org.mcq.spring6restmvc.mapper.CustomerMapper;
import org.mcq.spring6restmvc.model.BeerDTO;
import org.mcq.spring6restmvc.model.CustomerDTO;
import org.mcq.spring6restmvc.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.listCustomers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.listCustomers();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerController.getCustomerById(customer.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().name("New Customer").build();
        ResponseEntity responseEntity = customerController.createCustomer(customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        String strUUID = locationUUID[3].substring(8);
        UUID savedUUID = UUID.fromString(strUUID);

        Customer customer = customerRepository.findById(savedUUID).get();
        assertThat(customer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String name = "UPDATED";
        customerDTO.setName(name);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(name);
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
           customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteByIdFound() {
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteById(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.deleteById(UUID.randomUUID()));
    }

//    @Rollback
//    @Transactional
//    @Test
//    void testPatchById() {
//        Beer beer = beerRepository.findAll().get(0);
//        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
//        beerDTO.setId(null);
//        beerDTO.setVersion(null);
//        final String name = "PATCHED NAME";
//        final String upc = "PATCHED UPC";
//        beerDTO.setName(name);
//        beerDTO.setUpc(upc);
//
//        ResponseEntity responseEntity = beerController.updateBeerPatchById(beer.getId(), beerDTO);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
//
//        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
//        assertThat(updatedBeer.getName()).isEqualTo(name);
//        assertThat(updatedBeer.getUpc()).isEqualTo(upc);
//    }
//
//    @Test
//    void testPatchByIdNotFound() {
//        assertThrows(NotFoundException.class, () -> beerController.updateBeerPatchById(UUID.randomUUID(), BeerDTO.builder().build()));
//    }

    @Rollback
    @Transactional
    @Test
    void testPatchById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String name = "PATCHED NAME";
        customerDTO.setName(name);

        ResponseEntity responseEntity = customerController.updateCustomerPatchById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(name);
    }

    @Test
    void testPatchByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.updateCustomerPatchById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }
}
