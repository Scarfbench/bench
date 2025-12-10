/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jakarta.tutorial.batch.webserverlog.items;

/**
 * Represents a raw line from the web server log file.
 * Used as the item type for the ItemReader.
 */
public class LogLine {

    private final String datetime;
    private final String ipaddr;
    private final String browser;
    private final String url;

    /**
     * Parses a log line in the format: "datetime, ipaddr, browser, url"
     */
    public LogLine(String line) {
        String[] parts = line.split(", ");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid log line format: " + line);
        }
        this.datetime = parts[0];
        this.ipaddr   = parts[1];
        this.browser  = parts[2];
        this.url        = parts[3];
    }

    /**
     * For testing / manual construction (not used in the batch job)
     */
    public LogLine(String datetime, String ipaddr, String browser, String url) {
        this.datetime = datetime;
        this.ipaddr   = ipaddr;
        this.browser  = browser;
        this.url      = url;
    }

    @Override
    public String toString() {
        return datetime + " " + ipaddr + " " + browser + " " + url;
    }

    public String getDatetime() { return datetime; }
    public String getIpaddr()   { return ipaddr; }
    public String getBrowser()  { return browser; }
    public String getUrl()      { return url; }
}