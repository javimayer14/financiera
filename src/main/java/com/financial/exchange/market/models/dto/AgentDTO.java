package com.financial.exchange.market.models.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class AgentDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String username;
  private String name;
  private String surname;

}
