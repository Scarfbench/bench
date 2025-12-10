/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.batch.webserverlog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import jakarta.batch.api.chunk.ItemReader;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.tutorial.batch.webserverlog.items.LogLine;

/* Reads lines from the input log file */
@Dependent
@Named("LogLineReader")
public class LogLineReader implements ItemReader {

    private ItemNumberCheckpoint checkpoint;
    private String fileName;
    private BufferedReader breader;

    @Inject
    private JobContext jobCtx;

    public LogLineReader() {}

    @Override
    public void open(Serializable ckpt) throws Exception {
        if (ckpt == null) {
            checkpoint = new ItemNumberCheckpoint();
        } else {
            checkpoint = (ItemNumberCheckpoint) ckpt;
        }

        fileName = jobCtx.getProperties().getProperty("log_file_name");

        // In Quarkus: files in src/main/resources are on the classpath
        InputStream iStream = Thread.currentThread()
                                   .getContextClassLoader()
                                   .getResourceAsStream(fileName);

        if (iStream == null) {
            throw new IllegalStateException("Cannot find resource on classpath: " + fileName);
        }

        breader = new BufferedReader(new InputStreamReader(iStream));

        // Skip already-processed lines on restart
        for (int i = 0; i < checkpoint.getLineNum(); i++) {
            breader.readLine();
        }
    }

    @Override
    public void close() throws Exception {
        if (breader != null) {
            breader.close();
        }
    }

    @Override
    public Object readItem() throws Exception {
        String entry = breader.readLine();
        if (entry != null) {
            checkpoint.nextLine();
            return new LogLine(entry);
        } else {
            return null; // end of data
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return checkpoint;
    }
}