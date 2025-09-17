package jakarta.tutorial.web.dukeetf;

import jakarta.inject.Inject;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/dukeetf"}, asyncSupported = true)
public class DukeETFServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger("DukeETFServlet");
    private static final long serialVersionUID = 2114153638027156979L;

    @Inject
    PriceVolumeService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger.log(Level.INFO, "Servlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html"); // keep original type
        final AsyncContext acontext = request.startAsync();
        acontext.setTimeout(0);

        acontext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent ae) {
                logger.log(Level.INFO, "Connection closed.");
            }
            @Override
            public void onTimeout(AsyncEvent ae) {
                logger.log(Level.INFO, "Connection timeout.");
            }
            @Override
            public void onError(AsyncEvent ae) {
                logger.log(Level.INFO, "Connection error.");
            }
            @Override
            public void onStartAsync(AsyncEvent ae) { }
        });

        // hand the pending response to the scheduler service
        service.register(acontext);
    }
}
