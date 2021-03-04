package com.financial.exchange.market.models.dto;

import java.io.Serializable;

import com.financial.exchange.market.models.entity.CommissionType;

import lombok.Data;

@Data
public class CommissionPersonDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private CommissionType commissionType;
  private Float commissionAmount;

  private PersonDTO person;
}
