package com.velocity.ims.service;

import com.velocity.ims.model.Supplier;
import com.velocity.ims.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {
  @Autowired
  private SupplierRepository supplierRepository;

  public Supplier getSupplier(Long id) {
    return supplierRepository.findById(id).orElse(null);
  }

}
