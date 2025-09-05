package org.apache.struts.examples.mailreader2.dto;

import org.apache.struts.examples.mailreader2.dao.Subscription;
import jakarta.validation.constraints.NotBlank;

public class SubscriptionForm {
    private String task;
    private String user;
    @NotBlank(message = "{error.host.required}")
    private String host;
    @NotBlank(message = "{error.username.required}")
    private String username;
    @NotBlank(message = "{error.password.required}")
    private String password;
    @NotBlank(message = "{error.type.invalid}")
    private String type;
    private boolean autoConnect;

    public static SubscriptionForm from(Subscription subscription) {
        SubscriptionForm form = new SubscriptionForm();
        form.setHost(subscription.getHost());
        form.setUsername(subscription.getUsername());
        form.setPassword(subscription.getPassword());
        form.setType(subscription.getType());
        form.setAutoConnect(subscription.getAutoConnect());
        form.setUser(subscription.getUser().getUsername());

        return form;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

