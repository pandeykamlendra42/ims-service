package com.velocity.ims.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velocity.ims.domain.ListProductResponse;
import com.velocity.ims.domain.ProductResponse;
import com.velocity.ims.domain.request.CreateProductRequest;
import com.velocity.ims.domain.request.UpdateProductRequest;
import com.velocity.ims.exception.ApiErrorException;
import com.velocity.ims.model.Product;
import com.velocity.ims.model.Supplier;
import com.velocity.ims.repository.ProductRepository;
import com.velocity.ims.repository.specification.ProductSpecification;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ProductService {
  private static final int DEFAULT_PAGE_SIZE = 1000;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private SupplierService supplierService;
  @Value("${product.max-allowed-images:10}")
  private Integer MAX_ALLOWED_IMAGES;

  public ProductResponse getProductById(UUID productId) {
    log.info("Fetching product response for productId {}", productId);
    Product product = findProductById(productId);
    return entityToResponse(product);
  }

  public ListProductResponse getAllProductsByWithFilter(Long supplierId, BigDecimal minPrice, BigDecimal maxPrice) {
    log.info("Fetching all products for supplierId {}, minPrice {}, maxPrice {}", supplierId, minPrice, maxPrice);
    Specification<Product> specs = Specification.where(ProductSpecification.isDeleted(false))
        .and(ProductSpecification.hasSupplierId(supplierId))
        .and(ProductSpecification.hasPriceRange(minPrice, maxPrice));

    Pageable pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE,
        Sort.by(Sort.Direction.DESC, "modifiedAt"));

    Page<Product> products = productRepository.findAll(specs, pageable);
    ListProductResponse listProduct = new ListProductResponse();
    listProduct.setCount(Math.toIntExact(products.getTotalElements()));
    listProduct.setProducts(products.stream().map(this::entityToResponse).collect(Collectors.toList()));
    return listProduct;
  }

  public ProductResponse createProduct(CreateProductRequest productRequest) {
    log.info("Started creating a new product with the provided details {}", productRequest);
    Supplier supplier = supplierService.getSupplier(productRequest.getSupplierId());
    if (ObjectUtils.isEmpty(supplier)) {
      log.error("Supplier doesn't exists for Id {}", productRequest.getSupplierId());
      throw new ApiErrorException("Invalid input, supplier not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (!ObjectUtils.isEmpty(productRequest.getImages()) && productRequest.getImages().size() > MAX_ALLOWED_IMAGES) {
      log.error("Per product image limit exceeded");
      throw new ApiErrorException("Only " + MAX_ALLOWED_IMAGES + " images are allowed per product", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    Product product = objectMapper.convertValue(productRequest, Product.class);
    product = productRepository.save(product);
    log.info("Completed creating a new product with id {}", product.getId());
    return entityToResponse(product);
  }

  public ProductResponse updateProduct(UUID productId, UpdateProductRequest productRequest) {
    log.info("Started updating product with the provided details {}", productRequest);
    Product product = findProductById(productId);
    if (StringUtils.hasText(productRequest.getName())) {
      product.setName(productRequest.getName());
    }
    if (StringUtils.hasText(productRequest.getName())) {
      product.setName(productRequest.getName());
    }
    if (!ObjectUtils.isEmpty(productRequest.getPrice())) {
      product.setPrice(productRequest.getPrice());
    }
    if (!ObjectUtils.isEmpty(productRequest.getImages())) {
      if (productRequest.getImages().size() > MAX_ALLOWED_IMAGES) {
        log.error("Per product image limit exceeded");
        throw new ApiErrorException("Only " + MAX_ALLOWED_IMAGES + " images are allowed per product", HttpStatus.UNPROCESSABLE_ENTITY);
      }
      product.setImages(productRequest.getImages());
    }
    product = productRepository.save(product);
    log.info("Completed updating product with the provided details {}", product.getId());
    return entityToResponse(product);
  }

  public ProductResponse adjustStockQuantity(UUID productId, Integer quantity) {
    log.info("Updating stock quantity for productId {}", productId);
    Product product = findProductById(productId);
    product.setStockQuantity(quantity);
    product = productRepository.save(product);
    return entityToResponse(product);
  }

  public void deleteProduct(UUID productId) {
    log.info("Deleting product for id {}", productId);
    Product product = findProductById(productId);
    product.setIsDeleted(Boolean.TRUE);
    productRepository.save(product);
  }

  private Product findProductById(UUID productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ApiErrorException("Resource doesn't exists", HttpStatus.BAD_REQUEST));
  }

  private ProductResponse entityToResponse(Product product) {
    return objectMapper.convertValue(product, ProductResponse.class);
  }

}
