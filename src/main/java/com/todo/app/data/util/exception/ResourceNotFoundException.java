package com.todo.app.data.util.exception;

import com.todo.app.data.util.base.AbstractModel;
import com.todo.app.data.util.base.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends DataException {
    public ResourceNotFoundException(Class<? extends AbstractModel<?>> modelClass, long id) {
        super(String.format(
                "Model of class %s with id %d not found",
                modelClass.getSimpleName(), id));
    }

    public ResourceNotFoundException(Class<? extends AbstractModel<?>> modelClass, String attrName, Object value) {
        super(String.format(
                "Model of class %s with attribute %s equal %s not found",
                modelClass.getSimpleName(), attrName, value));
    }

    public ResourceNotFoundException(
            Class<? extends AbstractModel<?>> owner, long ownerId,
            Class<? extends AbstractModel<?>> dependent, long dependentId) {
        super(String.format(
                "Model %s with id %d does not have model %s with id %d",
                owner.getSimpleName(), ownerId, dependent.getSimpleName(), dependentId));
    }
}
