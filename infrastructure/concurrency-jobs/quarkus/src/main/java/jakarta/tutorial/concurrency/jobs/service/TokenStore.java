/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 *
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v1.0, which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.concurrency.jobs.service;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Quarkus-migrated TokenStore using CDI
 * 
 * @author markito
 */
@ApplicationScoped
public class TokenStore implements Serializable {

    private static final Logger logger = Logger.getLogger(TokenStore.class.getName());
    private final Set<String> store;

    public TokenStore() {
        this.store = ConcurrentHashMap.newKeySet();
        logger.info("TokenStore initialized");
    }

    public void put(String key) {
        store.add(key);
        logger.log(Level.FINE, "Token added: {0}", key);
    }

    public boolean isValid(String key) {
        boolean valid = store.contains(key);
        logger.log(Level.FINE, "Token validation for {0}: {1}", new Object[] { key, valid });
        return valid;
    }

    public void remove(String key) {
        store.remove(key);
        logger.log(Level.FINE, "Token removed: {0}", key);
    }

    public int size() {
        return store.size();
    }
}
