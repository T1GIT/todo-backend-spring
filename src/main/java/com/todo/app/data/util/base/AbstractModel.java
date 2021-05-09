package com.todo.app.data.util.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.function.Consumer;


@MappedSuperclass
public abstract class AbstractModel<T extends AbstractModel<T>> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    public AbstractModel() {
    }

    public AbstractModel(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public T edit(Consumer<T> editor) {
        editor.accept((T) this);
        return (T) this;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "id=" + id +
                '}';
    }
}
