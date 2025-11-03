package spring.examples.tutorial.cart;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import spring.examples.tutorial.cart.common.BookException;
import spring.examples.tutorial.cart.common.Cart;
import java.util.List;

// spring boot 3 / spring 6 removed support for remote method invocation
// so switched to REST
@RestController
@RequestMapping("/cart")
public class CartController {

    private final Cart cart;

    public CartController(Cart cart) {
        this.cart = cart;
    }

    @PostMapping("/initialize")
    public void initialize(@RequestParam("person") String person,
            @RequestParam(value = "id", required = false) String id)
            throws BookException {
        if (id == null) {
            cart.initialize(person);
        } else {
            cart.initialize(person, id);
        }
    }

    @PostMapping("/add")
    public void addBook(@RequestParam("title") String title) {
        cart.addBook(title);
    }

    @DeleteMapping("/remove")
    public void removeBook(@RequestParam("title") String title) throws BookException {
        cart.removeBook(title);
    }

    @GetMapping("/contents")
    public List<String> getContents() {
        return cart.getContents();
    }

    @PostMapping("/clear")
    public void checkout(HttpSession session) {
        cart.remove();
        session.invalidate();
    }

}
