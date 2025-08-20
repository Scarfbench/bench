package com.ibm.websphere.samples.daytrader.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class TradeListeners {

    @JmsListener(destination = "TradeBrokerQueue", containerFactory = "queueFactory")
    public void onBrokerQueueMessage(String msg) {
        // TODO: translate existing MDB logic here
        System.out.println("[TradeBrokerQueue] " + msg);
    }

    @JmsListener(destination = "TradeStreamerTopic", containerFactory = "topicFactory")
    public void onStreamerTopicEvent(String msg) {
        // TODO: translate existing MDB logic here
        System.out.println("[TradeStreamerTopic] " + msg);
    }
}