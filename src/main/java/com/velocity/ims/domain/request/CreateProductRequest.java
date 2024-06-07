package com.velocity.ims.domain.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class CreateProductRequest {

  @NotEmpty(message = "Name is a required field")
  private String name;

  @NotNull(message = "Supplier can't be null")
  private Long supplierId;

  private BigDecimal price;

  private Integer stockQuantity;

  private List<String> images;
}
