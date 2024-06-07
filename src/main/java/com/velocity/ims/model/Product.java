package com.velocity.ims.model;

import com.velocity.ims.model.persistence.BaseEntityUUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product", indexes = {
    @Index(name = "product_supplier_id_index", columnList = "supplier_id")
})
public class Product extends BaseEntityUUID {
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "supplier_id", nullable = false)
  private Long supplierId;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "stock_quantity")
  private Integer stockQuantity;

  @Column(name = "images")
  private List<String> images;
}
