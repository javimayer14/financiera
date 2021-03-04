package com.financial.exchange.market.models.dto;

import java.io.Serializable;

import lombok.Data;


/**
 * BrokerOperationBasicInfoDTO
 */
@Data
public class BrokerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    Long id;
    String userName;
    String name;
    String surName;
    
}