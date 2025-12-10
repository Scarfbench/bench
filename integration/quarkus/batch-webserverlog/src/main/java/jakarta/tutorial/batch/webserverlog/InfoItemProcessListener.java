
/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.batch.webserverlog;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.batch.api.chunk.listener.ItemProcessListener;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.tutorial.batch.webserverlog.items.LogLine;

/**
 * Logs information about each item being processed in the chunk step.
 */
@Dependent
@Named("InfoItemProcessListener")
public class InfoItemProcessListener implements ItemProcessListener {

    private static final Logger logger = Logger.getLogger(InfoItemProcessListener.class.getName());

    @Override
    public void beforeProcess(Object item) throws Exception {
        LogLine logLine = (LogLine) item;
        logger.info("Processing entry: " + logLine);
    }

    @Override
    public void afterProcess(Object item, Object result) throws Exception {
        // Nothing to do here in this example
    }

    @Override
    public void onProcessError(Object item, Exception ex) throws Exception {
        LogLine logLine = (LogLine) item;
        logger.log(Level.WARNING, "Error processing entry: " + logLine, ex);
    }
}