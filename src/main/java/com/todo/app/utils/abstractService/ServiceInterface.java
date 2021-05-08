package com.todo.app.utils.abstractService;


import com.todo.app.utils.AbstractModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ServiceInterface<ModelClass extends AbstractModel> {

    Optional<ModelClass> getById(Long id);

    ModelClass add(ModelClass model);

    void delete(ModelClass model);

    ModelClass update(ModelClass model);

    List<ModelClass> getAll();

    Page<ModelClass> getAll(Pageable pageable);

}
