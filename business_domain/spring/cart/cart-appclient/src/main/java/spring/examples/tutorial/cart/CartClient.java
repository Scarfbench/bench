package spring.examples.tutorial.cart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class CartClient {

    private final String baseUrl;

    public CartClient(@Value("${app.cart.url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void doCartOperations() {
        SessionAwareRestTemplate sessionTemplate = new SessionAwareRestTemplate();
        RestTemplate restTemplate = sessionTemplate.getRestTemplate();

        restTemplate.postForEntity(baseUrl + "/initialize?person={person}&id={id}", null,
                Void.class, "Duke d'Url", "123");

        restTemplate.postForEntity(baseUrl + "/add?title={title}", null, Void.class,
                "Infinite Jest");
        restTemplate.postForEntity(baseUrl + "/add?title={title}", null, Void.class, "Bel Canto");
        restTemplate.postForEntity(baseUrl + "/add?title={title}", null, Void.class,
                "Kafka on the Shore");

        List<String> books = restTemplate.exchange(
                baseUrl + "/contents",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}).getBody();

        books.forEach(title -> System.out
                .println("Retrieving book title from cart: " + title));

        System.out.println("Removing \"Gravity's Rainbow\" from cart.");
        restTemplate.delete(baseUrl + "/remove?title={title}", "Gravity's Rainbow");

        restTemplate.postForEntity(baseUrl + "/clear", null, Void.class);
    }

}
