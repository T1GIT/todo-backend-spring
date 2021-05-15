package com.todo.app.data.util.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel<T extends AuditModel<T>> extends AbstractModel<T> {

    @JsonIgnore
    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false)
    protected Date createdAt = new Date();

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    protected Date updatedAt = new Date();

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
