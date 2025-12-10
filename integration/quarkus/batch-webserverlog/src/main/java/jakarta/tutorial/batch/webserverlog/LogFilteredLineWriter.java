/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.batch.webserverlog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.List;

import jakarta.batch.api.chunk.ItemWriter;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.tutorial.batch.webserverlog.items.LogFilteredLine;

/**
 * Writes the filtered mobile/tablet log lines to filtered_file_name
 * (e.g. filtered1.txt). On restart it appends instead of overwriting.
 */
@Dependent
@Named("LogFilteredLineWriter")
public class LogFilteredLineWriter implements ItemWriter {

    private String fileName;
    private BufferedWriter bwriter;

    @Inject
    private JobContext jobCtx;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        fileName = jobCtx.getProperties().getProperty("filtered_file_name");

        // Append on restart, overwrite on first run
        boolean append = (checkpoint != null);
        bwriter = new BufferedWriter(new FileWriter(fileName, append));
    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (Object obj : items) {
            LogFilteredLine line = (LogFilteredLine) obj;
            bwriter.write(line.toString());
            bwriter.newLine();
        }
    }

    @Override
    public void close() throws Exception {
        if (bwriter != null) {
            bwriter.close();
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        // We don't need to store anything here because we always append/overwrite correctly
        // Returning null is perfectly valid for writers that are restart-safe this way
        return null;
    }
}