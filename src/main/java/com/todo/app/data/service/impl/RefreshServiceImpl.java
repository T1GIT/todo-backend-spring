package com.todo.app.data.service.impl;

import com.todo.app.data.model.Refresh;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.RefreshRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.RefreshService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.KeyGenerator;
import com.todo.app.security.util.enums.KeyLength;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class RefreshServiceImpl implements RefreshService {

    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;

    public RefreshServiceImpl(RefreshRepository refreshRepository, UserRepository userRepository) {
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Refresh create(long userId) throws ResourceNotFoundException {
        return refreshRepository.saveAndFlush(
                userRepository.findById(userId).map(user ->
                        new Refresh().edit(r -> {
                            r.setValue(genValue());
                            user.addRefresh(r);
                        })
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public Refresh update(String value) throws ResourceNotFoundException {
        return refreshRepository.saveAndFlush(
                refreshRepository.findByValue(value).map(refresh ->
                        refresh.edit(r -> r.setValue(genValue()))
                ).orElseThrow(() -> new ResourceNotFoundException(Refresh.class, "value", value)));
    }

    @Override
    public void delete(String value) throws ResourceNotFoundException {
        refreshRepository.findByValue(value).ifPresent(refreshRepository::delete);
    }

    private String genValue() {
        String value;
        do {
            value = KeyGenerator.string(KeyLength.REFRESH);
        } while (refreshRepository.existsByValue(value));

        return value;
    }
}
