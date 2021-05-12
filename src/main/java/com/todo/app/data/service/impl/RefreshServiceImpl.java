package com.todo.app.data.service.impl;

import com.todo.app.data.model.Category;
import com.todo.app.data.model.Refresh;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.RefreshRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.RefreshService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.KeyGenerator;
import com.todo.app.security.util.enums.SecretLength;
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
                        new Refresh() {{
                            setValue(genValue());
                            user.addRefresh(this);
                        }}
                ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public boolean exists(long userId, String value) throws ResourceNotFoundException {
        return userRepository.findById(userId).map(user ->
                user.getRefreshes().stream()
                        .anyMatch(r -> r.getValue().equals(value))
        ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
    }

    @Override
    public Refresh update(long userId, String value) throws ResourceNotFoundException {
        return refreshRepository.saveAndFlush(userRepository.findById(userId).map(user ->
                user.getRefreshes().stream()
                        .filter(r -> r.getValue().equals(value)).findAny().map(
                                refresh -> refresh.edit(r -> r.setValue(genValue()))
                ).orElseThrow(() -> new ResourceNotFoundException(Refresh.class, "value", value))
        ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId)));
    }

    @Override
    public void delete(long userId, String value) throws ResourceNotFoundException {
        userRepository.findById(userId).map(user -> user.getRefreshes().stream()
                .filter(r -> r.getValue().equals(value)).findAny().map(refresh ->
                        userRepository.saveAndFlush(
                                user.edit(u -> u.removeRefresh(refresh))))
        ).orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
    }

    private String genValue() {
        String value;
        do {
            value = KeyGenerator.string(SecretLength.REFRESH);
        } while (refreshRepository.existsByValue(value));
        return value;
    }
}
