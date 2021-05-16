package com.todo.app.data.util.base;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.function.Consumer;


@NoArgsConstructor
@Data
@MappedSuperclass
@JsonIgnoreProperties(value = "id")
public abstract class AbstractModel<T extends AbstractModel<T>> implements Serializable {

    @ApiModelProperty(name = "id", position = -1, accessMode = ApiModelProperty.AccessMode.READ_ONLY, example = "1")
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    public T edit(Consumer<T> editor) {
        editor.accept((T) this);
        return (T) this;
    }
}
