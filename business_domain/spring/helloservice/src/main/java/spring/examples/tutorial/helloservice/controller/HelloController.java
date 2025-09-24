package spring.examples.tutorial.helloservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.examples.tutorial.helloservice.service.HelloService;

// the original is a SOAP web service, so not exactly the same thing
@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam String name) {
        return helloService.sayHello(name);
    }
}
