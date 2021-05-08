package com.todo.app.utils.abstractService.impl;


import com.todo.app.utils.AbstractModel;
import com.todo.app.utils.abstractService.ServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public abstract class AbstractService
        <ModelClass extends AbstractModel,
                repositoryClass extends JpaRepository<ModelClass, Long>>
        implements ServiceInterface<ModelClass> {

    protected final repositoryClass repository;

    public AbstractService(repositoryClass repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ModelClass> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ModelClass add(ModelClass model) {
        return repository.saveAndFlush(model);
    }

    @Override
    public void delete(ModelClass model) {
        repository.delete(model);
    }

    @Override
    public ModelClass update(ModelClass model) {
        return repository.saveAndFlush(model);
    }

    @Override
    public List<ModelClass> getAll() {
        return repository.findAll();
    }

    public Page<ModelClass> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
