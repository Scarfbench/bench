package spring.examples.tutorial.cart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final CartClient cartClient;

    public Application(CartClient cartClient) {
        this.cartClient = cartClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            cartClient.doCartOperations();
        } catch (Exception ex) {
            System.err.println("Caught a BookException: " + ex.getMessage());
        }
    }

}
