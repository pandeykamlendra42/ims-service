package com.velocity.ims;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velocity.ims.domain.ListProductResponse;
import com.velocity.ims.domain.ProductResponse;
import com.velocity.ims.domain.request.CreateProductRequest;
import com.velocity.ims.exception.ApiErrorException;
import com.velocity.ims.model.Product;
import com.velocity.ims.model.Supplier;
import com.velocity.ims.repository.ProductRepository;
import com.velocity.ims.service.ProductService;
import com.velocity.ims.service.SupplierService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

  @MockBean
  private SupplierService supplierService;

  @MockBean
  private ProductRepository productRepository;

  @Autowired
  private ProductService productService;

  @Autowired
  private ObjectMapper mapper;

  @Test
  void testGetProductById() {
    // Arrange
    Product product = createSampleProduct(BigDecimal.valueOf(100));
    when(productRepository.findById(any())).thenReturn(Optional.of(product));

    // Act
    ProductResponse response = productService.getProductById(product.getId());

    // Assert
    Assertions.assertNotNull(response);
    Assertions.assertEquals(product.getId(), response.getId());
    Assertions.assertEquals(product.getName(), response.getName());
    Assertions.assertEquals(product.getPrice(), response.getPrice());
  }

  @Test
  void testGetAllProductsByWithFilter() {
    // Arrange
    Product product1 = createSampleProduct(BigDecimal.valueOf(100));
    Product product2 = createSampleProduct(BigDecimal.valueOf(200));
    Page<Product> productPage = new PageImpl<>(List.of(product1, product2));
    when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);

    // Act
    ListProductResponse response = productService.getAllProductsByWithFilter(
        null, BigDecimal.valueOf(100), null);

    // Assert
    Assertions.assertNotNull(response);
    Assertions.assertEquals(2, response.getCount());
    Assertions.assertEquals(2, response.getProducts().size());
  }

  @Test
  void testCreateProduct_Success() {
    // Arrange
    Supplier supplier = createSampleSupplier();
    when(supplierService.getSupplier(any())).thenReturn(supplier);
    Product product = createSampleProduct(BigDecimal.valueOf(234));
    CreateProductRequest request = mapper.convertValue(product, CreateProductRequest.class);
    when(productRepository.save(any())).thenReturn(product);
    // Act
    ProductResponse response = productService.createProduct(request);

    // Assert
    Assertions.assertNotNull(response);
    Assertions.assertEquals(request.getName(), response.getName());
    Assertions.assertEquals(request.getPrice(), response.getPrice());
  }

  @Test
  void testCreateProduct_InvalidInput() {
    // Arrange
    // Setting supplier as NULL
    when(supplierService.getSupplier(any())).thenReturn(null);
    Product product = createSampleProduct(BigDecimal.valueOf(234));
    CreateProductRequest request = mapper.convertValue(product, CreateProductRequest.class);
    // Act
    ApiErrorException ex = assertThrows(ApiErrorException.class, () -> {
      productService.createProduct(request);
    });
    // Assert
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getHttpStatus());
  }

  private Product createSampleProduct(BigDecimal price) {
    Product product = new Product();
    product.setId(UUID.randomUUID());
    product.setName("Sample Product");
    product.setSupplierId(1L);
    product.setPrice(price);
    product.setStockQuantity(100);
    return product;
  }

  private Supplier createSampleSupplier() {
    Supplier supplier = new Supplier();
    supplier.setId(1L);
    supplier.setName("Sample Supplier");
    return supplier;
  }
}
