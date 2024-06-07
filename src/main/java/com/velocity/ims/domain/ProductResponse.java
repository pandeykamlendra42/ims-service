package com.velocity.ims.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductResponse {

  private UUID id;

  private String name;

  private Long supplierId;

  private BigDecimal price;

  private Integer stockQuantity;

  private List<String> images;
}
