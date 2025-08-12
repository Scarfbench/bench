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
package struts.tutorial.billpayment.interceptor;

import java.util.logging.Logger;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Struts 2 interceptor that logs method invocations.
 * This replaces the CDI @Logged interceptor.
 */
public class LoggingInterceptor extends AbstractInterceptor {

    private static final Logger logger = Logger.getLogger(LoggingInterceptor.class.getCanonicalName());

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String className = invocation.getAction().getClass().getSimpleName();
        String methodName = invocation.getProxy().getMethod();

        logger.info("Entering method: " + className + "." + methodName);

        try {
            String result = invocation.invoke();
            logger.info("Exiting method: " + className + "." + methodName + " with result: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Exception in method: " + className + "." + methodName + " - " + e.getMessage());
            throw e;
        }
    }
}
