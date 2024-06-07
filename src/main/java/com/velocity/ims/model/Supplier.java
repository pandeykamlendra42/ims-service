package com.velocity.ims.model;

import com.velocity.ims.model.persistence.BaseEntityLongID;
import com.velocity.ims.model.persistence.BaseEntityUUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "supplier")
public class Supplier extends BaseEntityLongID {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "contact_information")
  private String contactInformation;

}
