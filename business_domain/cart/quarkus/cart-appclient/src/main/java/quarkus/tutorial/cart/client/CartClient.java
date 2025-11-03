package quarkus.tutorial.cart.client;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import quarkus.tutorial.cart.common.BookException;
import java.util.List;

@QuarkusMain
public class CartClient implements QuarkusApplication {

    @Inject
    @RestClient
    CartServiceClient cart;

    @Override
    public int run(String... args) throws Exception {
        try {
            cart.initialize("Duke d'Url", "123");
            cart.addBook("Infinite Jest");
            cart.addBook("Bel Canto");
            cart.addBook("Kafka on the Shore");

            List<String> bookList = cart.getContents();

            for (String title : bookList) {
                System.out.println("Retrieving book title from cart: " + title);
            }

            System.out.println("Removing \"Gravity's Rainbow\" from cart.");
            cart.removeBook("Gravity's Rainbow");

            cart.clearCart();

        } catch (WebApplicationException | BookException ex) {
            System.err.println("Caught a BookException: " + ex.getMessage());
        }

        return 0;
    }

}
