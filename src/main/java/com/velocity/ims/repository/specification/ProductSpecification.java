package com.velocity.ims.repository.specification;


import com.velocity.ims.model.Product;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

  public static Specification<Product> hasSupplierId(Long supplierId) {
    if (supplierId == null) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get("supplierId"), supplierId);
  }

  public static Specification<Product> hasPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
    if (minPrice != null && maxPrice != null) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price"),
          minPrice, maxPrice);
    }
    if (minPrice != null) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
          minPrice);
    }
    if (maxPrice != null) {
      return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"),
          maxPrice);
    }
    return null;
  }

  public static Specification<Product> isDeleted(boolean isDeleted) {
    return (root, query, cb) -> cb.equal(root.get("isDeleted"), isDeleted);
  }
}
