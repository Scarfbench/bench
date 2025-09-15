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
package jakarta.tutorial.concurrency.jobs.client;

import java.io.Serializable;
import java.util.logging.Logger;
import java.util.logging.Level;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * Quarkus-migrated client to REST service
 *
 * @author markito
 */
@Named
@RequestScoped
public class JobClient implements Serializable {
    private static final Logger logger = Logger.getLogger(JobClient.class.getName());
    private static final long serialVersionUID = 16472027766900196L;

    @Inject
    @RestClient
    JobServiceClient jobServiceClient;

    @ConfigProperty(name = "job-service.enabled", defaultValue = "true")
    boolean serviceEnabled;

    private String token;
    private int jobID;

    public String submit() {
        if (!serviceEnabled) {
            logger.warning("Job service is disabled");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Job service is currently disabled", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }

        try {
            final Response response = jobServiceClient.processJob(getJobID(), token);

            FacesMessage message;
            message = (response.getStatus() == 200)
                    ? new FacesMessage(FacesMessage.SEVERITY_INFO,
                            String.format("Job %d successfully submitted", getJobID()), null)
                    : new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            String.format("Job %d was NOT submitted (status: %d)", getJobID(), response.getStatus()),
                            null);

            FacesContext.getCurrentInstance().addMessage(null, message);
            logger.log(Level.INFO, "Job submission result: {0}", message.getSummary());

            clear();
            return "";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error submitting job " + getJobID(), e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    String.format("Error submitting job %d: %s", getJobID(), e.getMessage()), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
    }

    private void clear() {
        this.token = "";
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the jobID
     */
    public int getJobID() {
        return jobID;
    }

    /**
     * @param jobID the jobID to set
     */
    public void setJobID(int jobID) {
        this.jobID = jobID;
    }
}
