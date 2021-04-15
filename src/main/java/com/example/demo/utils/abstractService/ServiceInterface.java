package com.example.demo.utils.abstractService;


import com.example.demo.utils.AbstractModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
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
