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
package struts.tutorial.billpayment.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import struts.tutorial.billpayment.model.PaymentEvent;

/**
 * Implementation of PaymentService that handles payment processing.
 * This replaces the CDI event-driven approach with Spring-managed service.
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class.getCanonicalName());

    public PaymentServiceImpl() {
        logger.log(Level.INFO, "PaymentService created.");
    }

    @Override
    public void processCreditPayment(PaymentEvent event) {
        logger.log(Level.INFO, "PaymentService - Credit Handler: {0}",
                event.toString());

        // call a specific Credit handler class...
        // Here you would implement actual credit card processing logic
    }

    @Override
    public void processDebitPayment(PaymentEvent event) {
        logger.log(Level.INFO, "PaymentService - Debit Handler: {0}",
                event.toString());

        // call a specific Debit handler class...
        // Here you would implement actual debit card processing logic
    }
}
