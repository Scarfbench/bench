package org.apache.struts.examples.mailreader2.dto;

import java.util.Arrays;
import org.apache.struts.examples.mailreader2.dao.User;
import org.apache.struts.examples.mailreader2.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class RegistrationForm {

    private String task;

    @NotBlank(message = "{error.username.required}")
    private String username;

    @NotBlank(message = "{error.password.required}")
    @Size(min = 4, max = 10, message = "{errors.range}")
    private String password;

    @NotBlank(message = "{error.password2.required}")
    private String password2;

    @NotBlank(message = "{error.fullName.required}")
    private String fullName;

    @NotBlank(message = "{error.fromAddress.required}")
    @Email(message = "{errors.email}")
    private String fromAddress;

    @Email(message = "{errors.email}")
    private String replyToAddress;

    private SubscriptionDto[] subscriptions;

    public static RegistrationForm fromUser(User user) {
        RegistrationForm form = new RegistrationForm();
        form.setUsername(user.getUsername());
        form.setPassword(user.getPassword());
        form.setPassword2(user.getPassword());
        form.setFullName(user.getFullName());
        form.setFromAddress(user.getFromAddress());
        form.setReplyToAddress(user.getReplyToAddress());
        var subscriptions = Arrays.stream(user.getSubscriptions()).map(SubscriptionDto::from)
                .toArray(SubscriptionDto[]::new);
        form.setSubscriptions(subscriptions);
        form.setTask("Edit");

        return form;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getReplyToAddress() {
        return replyToAddress;
    }

    public void setReplyToAddress(String replyToAddress) {
        this.replyToAddress = replyToAddress;
    }

    public SubscriptionDto[] getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(SubscriptionDto[] subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        return "RegistrationForm [task=" + task + ", username=" + username + ", password="
                + password + ", password2=" + password2 + ", fullName=" + fullName
                + ", fromAddress=" + fromAddress + ", replyToAddress=" + replyToAddress
                + ", subscriptions=" + Arrays.toString(subscriptions) + "]";
    }

}

