package jakarta.tutorial.batch.webserverlog;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class JobController {

    @Inject Template index;
    @Inject Template jobstarted;
    @Inject JobBean jobBean;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return index.data("jobBean", jobBean);
    }

    // This is the new endpoint that actually starts the job
    @POST
    @Path("start-job")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance startJob() {
        jobBean.startBatchJob();           // ← This was missing!
        return jobstarted.data("jobBean", jobBean);
    }

    @GET
    @Path("jobstarted")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance jobstarted() {
        return jobstarted.data("jobBean", jobBean);
    }
}