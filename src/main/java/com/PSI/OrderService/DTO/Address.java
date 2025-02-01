package com.PSI.OrderService.DTO;

import lombok.Data;

@Data
public class Address {

    private long id;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String country;
    private String postalCode;
    private String stateOrRegion;
}
