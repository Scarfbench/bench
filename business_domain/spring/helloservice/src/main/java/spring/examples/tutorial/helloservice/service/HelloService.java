package spring.examples.tutorial.helloservice.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final String message = "Hello, ";

    public String sayHello(String name) {
        return message + name + ".";
    }
}
