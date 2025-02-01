package com.PSI.OrderService.DTO;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TicketDetailsDTO {

    //ticketId
    private long id;
    //orderId
    private Long orderId;

    //eventInfo
    private String eventName;
    private Timestamp eventStart;
    private String venueName;
    private String venueFacilityName;
    private Address venueAddress;

    //seatInfo
    private String seatInfo;
    private String sectionInfo;

    //ticketInfo
    private String ticketValidity;
    private String ticketReservationState;
    private double price;
    private String qrCode;
}
