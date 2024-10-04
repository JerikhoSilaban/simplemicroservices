package com.jerikho.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingleCustomerResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 8196615682798319208L;

    private String firstName;
    private String lastName;
    private String email;
}
