package com.todo.app.data.util.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel<T extends AuditModel<T>> extends AbstractModel<T> {

    @Setter(AccessLevel.NONE) @EqualsAndHashCode.Exclude
    @JsonIgnore
    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false)
    protected Date createdAt = new Date();

    @Setter(AccessLevel.NONE) @EqualsAndHashCode.Exclude
    @JsonIgnore
    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    protected Date updatedAt = new Date();

}
