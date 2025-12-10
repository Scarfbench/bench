/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.batch.webserverlog;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.JobExecution;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

@Named
@SessionScoped
public class JobBean implements Serializable {

    private static final long serialVersionUID = 3712686178567411830L;
    private long execID = -1;
    private JobOperator jobOperator;

    public JobBean() {
        jobOperator = BatchRuntime.getJobOperator();
    }

    public String getInputLog() {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("log1.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            sb.append("Error reading log file: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public String startBatchJob() {
        execID = jobOperator.start("webserverlog", null);
        return "jobstarted";
    }

    public String getJobStatus() {
        if (execID == -1) return "NOT_STARTED";
        JobExecution execution = jobOperator.getJobExecution(execID);
        return execution.getBatchStatus().toString();
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(getJobStatus());
    }

    // This is now a PROPERTY — Qute allows {jobBean.resultText}
    public String getResultText() {
        if (!isCompleted()) return "";

        try {
            String content = Files.readString(Paths.get("result1.txt")).trim();
            String[] parts = content.split(", ");
            if (parts.length != 3) return "Invalid result format";
            return String.format("%s purchases of %s tablet page views, (%s percent)",
                    parts[0], parts[1], parts[2]);
        } catch (Exception e) {
            return "Error reading result: " + e.getMessage();
        }
    }

    public long getExecID() { return execID; }
}