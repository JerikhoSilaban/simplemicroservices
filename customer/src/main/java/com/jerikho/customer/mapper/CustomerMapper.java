package com.jerikho.customer.mapper;

import com.jerikho.customer.dto.request.CustomerRegistrationRequest;
import com.jerikho.customer.dto.response.SingleCustomerResponse;
import com.jerikho.customer.entity.Customer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerMapper {
    @SneakyThrows
    public Customer constructEntity(CustomerRegistrationRequest request) {
        Optional.ofNullable(request).orElseThrow(() -> new RuntimeException("Customer request is null"));

        return Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
    }

    @SneakyThrows
    public Customer updateEntity(Customer entity, CustomerRegistrationRequest request) {
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());
        entity.setEmail(request.email());

        return entity;
    }

    public SingleCustomerResponse constructResponseDto(Customer customer) {
        return SingleCustomerResponse.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .build();
    }
}
