package jakarta.tutorial.web.dukeetf;

import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.AsyncContext;

import java.io.PrintWriter;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Replaces EJB TimerService. Keeps long-poll semantics:
 *  on each tick, write one update and complete every pending request. */
@ApplicationScoped
public class PriceVolumeService {
    private static final Logger log = Logger.getLogger("PriceVolumeService");

    private final Queue<AsyncContext> queue = new ConcurrentLinkedQueue<>();
    private final Random random = new Random();

    private volatile double price = 100.0;
    private volatile int volume = 300000;

    @PostConstruct
    void init() {
        log.log(Level.INFO, "Initializing scheduler-backed service.");
    }

    /** Called by the servlet to enqueue a pending response. */
    public void register(AsyncContext ctx) {
        queue.add(ctx);
        log.log(Level.INFO, "Connection open (queued).");
    }

    /** Every second: update values and flush the queue (one response per client). */
    @Scheduled(every = "1s")
    void tick() {
        price += 1.0 * (random.nextInt(100) - 50) / 100.0;
        volume += random.nextInt(5000) - 2500;
        flush();
    }

    private void flush() {
        String msg = String.format("%.2f / %d", price, volume);
        for (AsyncContext ctx; (ctx = queue.poll()) != null; ) {
            try {
                PrintWriter w = ctx.getResponse().getWriter();
                w.write(msg);
                w.flush();
                log.log(Level.INFO, "Sent: {0}", msg);
            } catch (Exception e) {
                log.log(Level.INFO, "Send failed: {0}", e.toString());
            } finally {
                try { ctx.complete(); } catch (Exception ignored) {}
            }
        }
    }
}
