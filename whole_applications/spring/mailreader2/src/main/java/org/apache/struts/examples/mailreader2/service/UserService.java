package org.apache.struts.examples.mailreader2.service;

import org.apache.struts.examples.mailreader2.Constants;
import org.apache.struts.examples.mailreader2.dao.ExpiredPasswordException;
import org.apache.struts.examples.mailreader2.dao.User;
import org.apache.struts.examples.mailreader2.dao.UserDatabase;
import org.apache.struts.examples.mailreader2.dto.RegistrationForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserDatabase userDatabase;

    public UserService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public User findUser(String username, String password) throws ExpiredPasswordException {
        // FIXME: Stupid testing hack to simulate expired password
        if (Constants.EXPIRED_PASSWORD_EXCEPTION.equals(username)) {
            throw new ExpiredPasswordException(Constants.EXPIRED_PASSWORD_EXCEPTION);
        }

        User user = userDatabase.findUser(username);
        if ((user != null) && !user.getPassword().equals(password)) {
            user = null;
        }

        return user;
    }

    public boolean exists(String username) {
        try {
            return userDatabase.findUser(username) != null;
        } catch (ExpiredPasswordException e) {
            // means it exists
            return true;
        }
    }

    public User create(RegistrationForm form) {
        User user = userDatabase.createUser(form.getUsername());
        user.setPassword(form.getPassword());
        user.setFullName(form.getFullName());
        user.setFromAddress(form.getFromAddress());
        user.setReplyToAddress(form.getReplyToAddress());

        save(user);

        return user;
    }

    public User update(RegistrationForm form) {
        User user = getAuthUser();
        user.setPassword(form.getPassword());
        user.setFullName(form.getFullName());
        user.setFromAddress(form.getFromAddress());
        user.setReplyToAddress(form.getReplyToAddress());

        save(user);

        return user;
    }

    public User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            try {
                return userDatabase.findUser(username);
            } catch (ExpiredPasswordException e) {
                LOG.error("Failed to retrieve authenticated user:", e);
            }
        }

        return null;
    }

    public void save(User user) {
        try {
            userDatabase.save();
        } catch (Exception e) {
            String message = Constants.LOG_DATABASE_SAVE_ERROR + user
                    .getUsername();
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

}
