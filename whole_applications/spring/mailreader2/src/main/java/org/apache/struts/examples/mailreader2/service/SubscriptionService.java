package org.apache.struts.examples.mailreader2.service;

import java.util.NoSuchElementException;
import org.apache.struts.examples.mailreader2.dao.Subscription;
import org.apache.struts.examples.mailreader2.dao.User;
import org.apache.struts.examples.mailreader2.dto.SubscriptionForm;
import org.apache.struts.examples.mailreader2.exceptions.NoAuthUserException;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final UserService userService;

    public SubscriptionService(UserService userService) {
        this.userService = userService;
    }

    public Subscription getSubscription(String host) {
        User user = userService.getAuthUser();
        if (user == null) {
            throw new NoAuthUserException();
        }
        return user.findSubscription(host);
    }

    public void delete(String host) {
        User user = userService.getAuthUser();
        if (user == null) {
            throw new NoAuthUserException();
        }
        Subscription subscription = user.findSubscription(host);
        if (subscription == null) {
            throw new NoSuchElementException(
                    String.format("No subscription with the host: `%s` found", host));
        }
        user.removeSubscription(subscription);

        userService.save(user);
    }

    public boolean exists(String host) {
        return getSubscription(host) != null;
    }

    public Subscription create(SubscriptionForm form) {
        User user = userService.getAuthUser();
        if (user == null) {
            throw new NoAuthUserException();
        }

        Subscription subscription = user.createSubscription(form.getHost());
        subscription.setAutoConnect(form.isAutoConnect());
        subscription.setPassword(form.getPassword());
        subscription.setUsername(form.getUsername());
        subscription.setType(form.getType());

        userService.save(user);

        return subscription;
    }

    public Subscription update(SubscriptionForm form) {
        String host = form.getHost();
        User user = userService.getAuthUser();
        if (user == null) {
            throw new NoAuthUserException();
        }
        Subscription subscription = user.findSubscription(host);
        if (subscription == null) {
            throw new NoSuchElementException(
                    String.format("No subscription with the host: `%s` found", host));
        }

        subscription.setAutoConnect(form.isAutoConnect());
        subscription.setPassword(form.getPassword());
        subscription.setUsername(form.getUsername());
        subscription.setType(form.getType());

        userService.save(user);

        return subscription;
    }

}
