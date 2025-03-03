package com.study.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity {

    /**
     * 생성자
     */
    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false, nullable = false)
    private String createdBy;

    /**
     * 수정자
     */
    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", nullable = false)
    private String lastModifiedBy;


    /**
     * 생성일시
     */
    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT", nullable = false)
    private LocalDateTime lastModifiedAt;

}
