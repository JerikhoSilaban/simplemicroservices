package com.jerikho.customer.service;

import com.jerikho.customer.dto.CustomerRegistrationRequest;
import com.jerikho.customer.dto.FraudCheckResponse;
import com.jerikho.customer.entity.Customer;
import com.jerikho.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.saveAndFlush(customer);


        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("Customer with id " + customer.getId() + " is already fraudster");
        }
    }

    public void getCustomer(Integer customerId) {
        System.out.println(customerRepository.findById(customerId).get());
    }
}
