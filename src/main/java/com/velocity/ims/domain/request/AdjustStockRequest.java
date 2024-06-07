package com.velocity.ims.domain.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class AdjustStockRequest {

  @NotNull(message = "Product can't be null")
  private UUID productId;

  private Integer stockQuantity;

}
