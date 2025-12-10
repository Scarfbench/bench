/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.batch.webserverlog;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.batch.api.listener.JobListener;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

/**
 * Simple job listener that logs when the whole job starts and finishes.
 */
@Dependent
@Named("InfoJobListener")
public class InfoJobListener implements JobListener {

    private static final Logger logger = Logger.getLogger(InfoJobListener.class.getName());

    @Override
    public void beforeJob() throws Exception {
        logger.info("The job is starting");
    }

    @Override
    public void afterJob() throws Exception {
        logger.info("The job has finished.");
    }
}