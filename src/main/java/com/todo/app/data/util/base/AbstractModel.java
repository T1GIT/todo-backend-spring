package com.todo.app.data.util.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.function.Consumer;


@NoArgsConstructor
@Data
@MappedSuperclass
public abstract class AbstractModel<T extends AbstractModel<T>> implements Serializable {

    @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    public T edit(Consumer<T> editor) {
        editor.accept((T) this);
        return (T) this;
    }
}
