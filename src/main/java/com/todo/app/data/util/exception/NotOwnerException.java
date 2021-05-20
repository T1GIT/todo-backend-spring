package com.todo.app.data.util.exception;

import com.todo.app.data.util.base.AbstractModel;
import com.todo.app.data.util.base.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotOwnerException extends DataException {
    public NotOwnerException(long userId, Class<? extends AbstractModel<?>> modelClass, long modelId) {
        super(String.format(
                "User with id %d is not an owner of the model %s with id %d",
                userId, modelClass.getSimpleName(), modelId));
    }
}
