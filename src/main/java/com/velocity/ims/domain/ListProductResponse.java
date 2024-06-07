package com.velocity.ims.domain;

import java.util.List;
import lombok.Data;

@Data
public class ListProductResponse {
  private int count;
  private List<ProductResponse> products;
}
