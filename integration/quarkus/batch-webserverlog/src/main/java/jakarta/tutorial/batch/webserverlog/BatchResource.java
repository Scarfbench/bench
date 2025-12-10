package jakarta.tutorial.batch.webserverlog;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.JobExecution;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;   // <--- THIS WAS MISSING
// or use: import java.util.HashMap;

@Path("/batch")
public class BatchResource {

    @Inject
    JobBean jobBean;

    @POST
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startJob() {
        jobBean.startBatchJob();
        return Response.ok(Map.of("jobId", jobBean.getExecID())).build(); // now works
    }

    @GET
    @Path("/status/{jobId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(@PathParam("jobId") long jobId) { // now works
        JobOperator operator = BatchRuntime.getJobOperator();
        JobExecution execution = operator.getJobExecution(jobId);
        return Response.ok(Map.of("status", execution.getBatchStatus().toString())).build();
    }
}