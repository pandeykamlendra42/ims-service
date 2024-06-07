package com.velocity.ims.model.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public abstract class BaseEntity {

  @Getter
  @Setter
  @CreatedDate

  @Column(name = "created_at", nullable = false, updatable = false)
  private ZonedDateTime createdAt = ZonedDateTime.now(ZoneId.of("UTC"));

  @Getter
  @Setter
  @LastModifiedDate
  @UpdateTimestamp
  @Column(name = "modified_at", nullable = false)
  private ZonedDateTime modifiedAt = ZonedDateTime.now(ZoneId.of("UTC"));

  @Getter
  @Setter
  @Column(name = "is_deleted", columnDefinition = "boolean default false")
  private Boolean isDeleted = false;

}
