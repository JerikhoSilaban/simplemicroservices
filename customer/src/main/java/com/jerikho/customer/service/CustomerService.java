package com.jerikho.customer.service;

import com.jerikho.customer.dto.request.CustomerRegistrationRequest;
import com.jerikho.customer.dto.response.SingleCustomerResponse;
import com.jerikho.customer.dto.response.FraudCheckResponse;
import com.jerikho.customer.dto.request.GetByIdRequest;
import com.jerikho.customer.entity.Customer;
import com.jerikho.customer.mapper.CustomerMapper;
import com.jerikho.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RestTemplate restTemplate;

    public SingleCustomerResponse registerCustomer(CustomerRegistrationRequest request) {

        Customer entity = Optional.ofNullable(request.id())
                .flatMap(customerId -> customerRepository.findById(customerId))
                .map(customer -> customerMapper.updateEntity(customer, request))
                .orElse(customerMapper.constructEntity(request));

        Customer entityResp = customerRepository.save(entity);

        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                entityResp.getId()
        );

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("Customer with id " + entityResp.getId() + " is already fraudster");
        }

        return SingleCustomerResponse.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }

    public SingleCustomerResponse getCustomer(GetByIdRequest request) {
        Customer entity = customerRepository.findById(request.getCustomerId()).orElse(Customer.builder().build());

        return SingleCustomerResponse.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }

    public List<SingleCustomerResponse> getAllCustomer() {
        List<Customer> entities = Optional.ofNullable(customerRepository.findAll()).orElse(Collections.emptyList());

        return entities.stream()
                .map(customer -> SingleCustomerResponse.builder()
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .email(customer.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public SingleCustomerResponse deleteCustomer(GetByIdRequest request) {
        Optional<Customer> optionalCustomer = customerRepository.findById(request.getCustomerId());

        if (optionalCustomer.isPresent()) {
            Customer entity = optionalCustomer.get();
            customerRepository.delete(entity);

            return SingleCustomerResponse.builder()
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .email(entity.getEmail())
                    .build();
        } else {
            return SingleCustomerResponse.builder().build();
        }
    }
}
