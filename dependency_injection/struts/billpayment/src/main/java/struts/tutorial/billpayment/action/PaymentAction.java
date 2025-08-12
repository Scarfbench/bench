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
package struts.tutorial.billpayment.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

import struts.tutorial.billpayment.model.PaymentEvent;
import struts.tutorial.billpayment.service.PaymentService;

/**
 * Struts 2 Action that handles payment operations.
 * This replaces the JSF PaymentBean and uses Spring DI instead of CDI.
 */
@Component("paymentAction")
@Scope("session")
public class PaymentAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(PaymentAction.class.getCanonicalName());
    private static final long serialVersionUID = 7130389273118012929L;

    @Autowired
    private PaymentService paymentService;

    private static final int DEBIT = 1;
    private static final int CREDIT = 2;
    private int paymentOption = DEBIT;

    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid value")
    private BigDecimal value;

    private Date datetime;

    /**
     * Default action method that shows the payment form.
     *
     * @return the input page
     */
    public String input() {
        return INPUT;
    }

    /**
     * Processes a payment.
     *
     * @return the response page location
     */
    public String pay() {
        this.setDatetime(Calendar.getInstance().getTime());

        PaymentEvent event = new PaymentEvent();
        event.setValue(value);
        event.setDatetime(datetime);

        switch (paymentOption) {
            case DEBIT:
                event.setPaymentType("Debit");
                paymentService.processDebitPayment(event);
                break;
            case CREDIT:
                event.setPaymentType("Credit");
                paymentService.processCreditPayment(event);
                break;
            default:
                logger.severe("Invalid payment option!");
                addActionError("Invalid payment option selected");
                return ERROR;
        }

        return SUCCESS;
    }

    /**
     * Resets the values in the form.
     */
    public String reset() {
        setPaymentOption(DEBIT);
        setValue(BigDecimal.ZERO);
        return INPUT;
    }

    // Getters and Setters
    public int getPaymentOption() {
        return this.paymentOption;
    }

    public void setPaymentOption(int paymentOption) {
        this.paymentOption = paymentOption;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
