package org.apache.struts.examples.mailreader2;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.struts.examples.mailreader2.exceptions.NoAuthUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ExceptionHandler(NoAuthUserException.class)
    public String handleNoAuthUserException(NoAuthUserException exception) {
        log.error("No authenticated user found:", exception);
        return "login";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        log.error("Error:", exception);

        model.addAttribute("errorMessage", exception.getMessage());

        // Stack trace as string
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        model.addAttribute("stackTrace", sw.toString());

        return "error";
    }

}
