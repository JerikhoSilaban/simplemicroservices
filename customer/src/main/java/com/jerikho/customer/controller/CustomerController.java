package com.jerikho.customer.controller;

import com.jerikho.customer.dto.request.CustomerRegistrationRequest;
import com.jerikho.customer.dto.request.GetByIdRequest;
import com.jerikho.customer.dto.response.SingleCustomerResponse;
import com.jerikho.customer.entity.Customer;
import com.jerikho.customer.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @CircuitBreaker(name = "registerCustomer", fallbackMethod = "registerCustomerFallback")
    @Retry(name = "registerCustomer")
    @RateLimiter(name = "registerCustomer")
    public ResponseEntity<SingleCustomerResponse> registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("New customer registration: {}", customerRegistrationRequest);

        SingleCustomerResponse response = customerService.registerCustomer(customerRegistrationRequest);

        return ResponseEntity.ok(response);
    }

    public String registerCustomerFallback(Exception e) {
        return "This is registerCustomer fallback";
    }

    @PostMapping(value = "/getById", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SingleCustomerResponse> getCustomer(@RequestBody GetByIdRequest getByIdRequest) {
        if (getByIdRequest.getCustomerId() == null) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Get customer by id: {}", getByIdRequest);

        SingleCustomerResponse response = customerService.getCustomer(getByIdRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/getAll", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SingleCustomerResponse>> getAllCustomer() {
        log.info("Get all customers");

        List<SingleCustomerResponse> response = customerService.getAllCustomer();

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/delete", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<SingleCustomerResponse> deleteCustomer(@RequestBody GetByIdRequest getByIdRequest) {
        if (getByIdRequest.getCustomerId() == null) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Get customer by id: {}", getByIdRequest);

        SingleCustomerResponse response = customerService.deleteCustomer(getByIdRequest);

        return ResponseEntity.ok(response);
    }
}
