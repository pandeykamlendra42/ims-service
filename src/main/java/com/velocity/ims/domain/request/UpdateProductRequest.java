package com.velocity.ims.domain.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class UpdateProductRequest {

  private String name;

  private BigDecimal price;

  private List<String> images;
}
