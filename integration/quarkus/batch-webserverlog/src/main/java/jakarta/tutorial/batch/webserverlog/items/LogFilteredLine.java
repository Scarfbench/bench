/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.batch.webserverlog.items;

/**
 * Represents a filtered log line (only IP address and requested URL).
 * This is the item written by LogFilteredLineWriter.
 */
public class LogFilteredLine {

    private final String ipaddr;
    private final String url;

    /** Used by the processor – from a full LogLine */
    public LogFilteredLine(LogLine ll) {
        this.ipaddr = ll.getIpaddr();
        this.url    = ll.getUrl();
    }

    /** Used only if someone reads the filtered file back (not needed in this job) */
    public LogFilteredLine(String line) {
        String[] parts = line.split(", ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid filtered line format: " + line);
        }
        this.ipaddr = parts[0];
        this.url    = parts[1];
    }

    @Override
    public String toString() {
        return ipaddr + ", " + url;
    }

    public String getIpaddr() { return ipaddr; }
    public String getUrl()    { return url; }
}