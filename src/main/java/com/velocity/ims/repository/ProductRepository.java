package com.velocity.ims.repository;

import com.velocity.ims.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
    extends PagingAndSortingRepository<Product, UUID>, JpaSpecificationExecutor<Product>, JpaRepository<Product, UUID> {

  Optional<List<Product>> findBySupplierId(@Param("supplierId") Long supplierId);

}
