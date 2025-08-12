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

import struts.tutorial.billpayment.model.PaymentEvent;

/**
 * Service interface for payment processing.
 */
public interface PaymentService {

    /**
     * Process a debit payment.
     *
     * @param event the payment event
     */
    void processDebitPayment(PaymentEvent event);

    /**
     * Process a credit payment.
     *
     * @param event the payment event
     */
    void processCreditPayment(PaymentEvent event);
}
