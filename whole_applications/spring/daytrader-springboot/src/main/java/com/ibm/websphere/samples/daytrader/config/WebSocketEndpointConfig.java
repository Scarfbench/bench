package com.ibm.websphere.samples.daytrader.config;

import com.springframework.context.annotation.Bean;
import com.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketEndpointConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
