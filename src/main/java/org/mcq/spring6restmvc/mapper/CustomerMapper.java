package org.mcq.spring6restmvc.mapper;

import org.mapstruct.Mapper;
import org.mcq.spring6restmvc.entities.Customer;
import org.mcq.spring6restmvc.model.CustomerDTO;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
