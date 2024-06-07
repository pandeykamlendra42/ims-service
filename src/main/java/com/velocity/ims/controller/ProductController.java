package com.velocity.ims.controller;

import com.velocity.ims.apispec.ApiOutput;
import com.velocity.ims.domain.request.CreateProductRequest;
import com.velocity.ims.domain.request.UpdateProductRequest;
import com.velocity.ims.service.ProductService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/products")
@RestController
@Slf4j
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping("/{id}")
  public ResponseEntity<ApiOutput<?>> getProductResponse(@PathVariable("id") UUID productId) {
    log.info("Fetching product for productId: {}", productId);
    return ResponseEntity.ok(new ApiOutput<>(productService.getProductById(productId)));
  }

  @GetMapping
  public ResponseEntity<ApiOutput<?>> getAllProduct(@RequestParam(value = "supplierId", required = false) Long supplierId,
                                                    @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                                                    @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice) {
    log.info("Fetching all products with provided filters");
    return ResponseEntity.ok(new ApiOutput<>(productService.getAllProductsByWithFilter(supplierId, minPrice, maxPrice)));
  }

  @PostMapping
  public ResponseEntity<ApiOutput<?>> createProduct(@Valid @RequestBody CreateProductRequest productRequest) {
    log.info("Creating a new product");
    return ResponseEntity.ok(new ApiOutput<>(productService.createProduct(productRequest)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiOutput<?>> updateProduct(@PathVariable("id") UUID productId,
                                                    @Valid @RequestBody UpdateProductRequest productRequest) {
    log.info("Updating product details for Id {}", productId);
    return ResponseEntity.ok(new ApiOutput<>(productService.updateProduct(productId, productRequest)));
  }

  @PatchMapping("/{id}/stock")
  public ResponseEntity<ApiOutput<?>> adjustStock(@PathVariable("id") UUID productId,
                                                  @RequestBody Integer stock) {
    log.info("Updating stock for Id {}", productId);
    return ResponseEntity.ok(new ApiOutput<>(productService.adjustStockQuantity(productId, stock)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiOutput<?>> deleteProduct(@PathVariable("id") UUID productId) {
    log.info("Deleting product details for Id {}", productId);
    productService.deleteProduct(productId);
    return ResponseEntity.ok(new ApiOutput<>());
  }
}
