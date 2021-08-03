package com.todo.app.data.service.impl;

import com.todo.app.data.model.Session;
import com.todo.app.data.model.User;
import com.todo.app.data.repo.SessionRepository;
import com.todo.app.data.repo.UserRepository;
import com.todo.app.data.service.SessionService;
import com.todo.app.data.util.exception.ResourceNotFoundException;
import com.todo.app.security.crypt.KeyGenerator;
import com.todo.app.security.token.RefreshProvider;
import com.todo.app.security.util.enums.KeyLength;
import com.todo.app.security.util.exception.ExpiredRefreshException;
import com.todo.app.security.util.exception.InvalidFingerprintException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;


@RequiredArgsConstructor
@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public Session create(long userId, String fingerprint) throws ResourceNotFoundException {
        checkUserById(userId);
        User user = userRepository.getOne(userId);
        sessionRepository.deleteAllByUserIdAndFingerprint(userId, fingerprint);
        Session session = new Session();
        session.setRefresh(genValue());
        session.setExpires(new Date(System.currentTimeMillis() + RefreshProvider.DURATION.toMillis()));
        session.setFingerprint(fingerprint);
        session.setUser(user);
        sessionRepository.saveAndFlush(session);
        return session;
    }

    // TODO: Add userId and jwt to get it
    @Override
    public Session update(String refresh, String fingerprint) {
        checkSessionByRefresh(refresh);
        Session session = sessionRepository.getByRefresh(refresh);
        checkSessionExpiresAndFingerprint(session, fingerprint);
        session.setRefresh(genValue());
        session.setExpires(new Date(System.currentTimeMillis() + RefreshProvider.DURATION.toMillis()));
        sessionRepository.saveAndFlush(session);
        return session;
    }

    @Override
    public void delete(String refresh) {
        checkSessionByRefresh(refresh);
        sessionRepository.deleteByRefresh(refresh);
    }

    private String genValue() {
        String value;
        do {
            value = KeyGenerator.string(KeyLength.REFRESH);
        } while (sessionRepository.existsByRefresh(value));
        return value;
    }

    private void checkUserById(long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId))
            throw new ResourceNotFoundException(User.class, userId);
    }
    
    private void checkSessionByRefresh(String refresh) throws ResourceNotFoundException {
        if (!sessionRepository.existsByRefresh(refresh))
            throw new ResourceNotFoundException(Session.class, "refresh", refresh);
    }

    private void checkSessionExpiresAndFingerprint(Session session, String fingerprint) throws ExpiredRefreshException, InvalidFingerprintException {
        if (session.getExpires().before(new Date()))
            throw new ExpiredRefreshException(session.getId(), session.getRefresh());
        if (!session.getFingerprint().equals(fingerprint))
            throw new InvalidFingerprintException(session, fingerprint);
    }
}
