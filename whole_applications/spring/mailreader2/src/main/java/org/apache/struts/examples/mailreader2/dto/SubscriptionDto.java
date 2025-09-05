package org.apache.struts.examples.mailreader2.dto;

import org.apache.struts.examples.mailreader2.dao.Subscription;

public class SubscriptionDto {

    private String host;
    private String username;
    private String password;
    private String type;
    private boolean autoConnect;

    public static SubscriptionDto from(Subscription subscription) {
        var dto = new SubscriptionDto();
        dto.setAutoConnect(subscription.getAutoConnect());
        dto.setHost(subscription.getHost());
        dto.setPassword(subscription.getPassword());
        dto.setType(subscription.getType());
        dto.setUsername(subscription.getUsername());

        return dto;
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

}
