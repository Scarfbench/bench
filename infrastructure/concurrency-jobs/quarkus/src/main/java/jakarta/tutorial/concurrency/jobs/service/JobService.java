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
package jakarta.tutorial.concurrency.jobs.service;

import java.util.UUID;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Quarkus JAX-RS Resource for Job Service
 * 
 * @author markito
 */
@ApplicationScoped
@Path("/JobService")
public class JobService {

    private static final Logger logger = Logger.getLogger(JobService.class.getName());
    // http header to check for valid tokens
    private static final String API_TOKEN_HEADER = "X-REST-API-Key";

    @Inject
    TokenStore tokenStore;

    @ConfigProperty(name = "job.high-priority.threads", defaultValue = "5")
    int highPriorityThreads;

    @ConfigProperty(name = "job.low-priority.threads", defaultValue = "2")
    int lowPriorityThreads;

    private ExecutorService highPrioExecutor;
    private ExecutorService lowPrioExecutor;

    @PostConstruct
    void initialize() {
        // Create thread pools with different priorities
        highPrioExecutor = Executors.newFixedThreadPool(highPriorityThreads, r -> {
            Thread t = new Thread(r, "high-priority-job-");
            t.setPriority(Thread.MAX_PRIORITY);
            return t;
        });

        lowPrioExecutor = Executors.newFixedThreadPool(lowPriorityThreads, r -> {
            Thread t = new Thread(r, "low-priority-job-");
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });

        logger.log(Level.INFO, "JobService initialized with {0} high-priority and {1} low-priority threads",
                new Object[] { highPriorityThreads, lowPriorityThreads });
    }

    @PreDestroy
    void shutdown() {
        if (highPrioExecutor != null) {
            highPrioExecutor.shutdown();
        }
        if (lowPrioExecutor != null) {
            lowPrioExecutor.shutdown();
        }
        logger.info("JobService shutdown completed");
    }

    @GET
    @Path("/token")
    public Response getToken() {
        // static token + dynamic token
        final String token = "123X5-" + UUID.randomUUID().toString();
        tokenStore.put(token);
        return Response.status(200).entity(token).build();
    }

    @POST
    @Path("/process")
    public Response process(final @HeaderParam(API_TOKEN_HEADER) String token,
            final @QueryParam("jobID") int jobID) {

        try {
            if (token != null && tokenStore.isValid(token)) {
                logger.info("Token accepted. Execution with high priority.");
                highPrioExecutor.submit(new JobTask("HIGH-" + jobID));
            } else {
                logger.log(Level.INFO, "Invalid or missing token! {0}", token);
                // requests without token, will be executed but without priority
                lowPrioExecutor.submit(new JobTask("LOW-" + jobID));
            }
        } catch (RejectedExecutionException ree) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("Job " + jobID + " NOT submitted. " + ree.getMessage()).build();
        }

        return Response.status(Response.Status.OK).entity("Job " + jobID + " successfully submitted.").build();
    }

    static class JobTask implements Runnable {

        private final String jobID;
        private final int JOB_EXECUTION_TIME = 10000;

        public JobTask(String id) {
            this.jobID = id;
        }

        @Override
        public void run() {
            try {
                logger.log(Level.INFO, "Task started {0}", jobID);
                Thread.sleep(JOB_EXECUTION_TIME); // 10 seconds per job
                logger.log(Level.INFO, "Task finished {0}", jobID);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, "Task interrupted", ex);
                Thread.currentThread().interrupt();
            }
        }
    }
}
