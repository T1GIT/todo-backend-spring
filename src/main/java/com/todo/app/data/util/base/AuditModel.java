package com.todo.app.data.util.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(name = "createdAt", nullable = false, updatable = false)
    protected Date createdAt = new Date();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    @Column(name = "updatedAt", nullable = false)
    protected Date updatedAt = new Date();

}
