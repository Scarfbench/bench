package jakarta.tutorial.batch.webserverlog;

import io.quarkus.qute.Template;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Templates {

    @Inject Template index;
    @Inject Template jobstarted;

    public io.quarkus.qute.TemplateInstance index(String inputLog) {
        return index.data("inputLog", inputLog);
    }

    public io.quarkus.qute.TemplateInstance jobstarted() {
        return jobstarted.instance();
    }
}