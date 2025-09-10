package org.apache.struts.examples.mailreader2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.struts.examples.mailreader2.dao.UserDatabase;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final MessageSource messageSource;
    private final UserDatabase userDatabase;

    public WelcomeController(MessageSource messageSource, UserDatabase userDatabase) {
        this.messageSource = messageSource;
        this.userDatabase = userDatabase;
    }

    @GetMapping("/")
    public String redirectToWelcome() {
        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public String welcome(Locale locale, Model model) {
        List<String> errors = new ArrayList<>();

        String message = messageSource.getMessage(Constants.ERROR_DATABASE_MISSING, null,
                Constants.ERROR_DATABASE_MISSING, locale);

        if (Constants.ERROR_DATABASE_MISSING.equals(message)) {
            errors.add(Constants.ERROR_MESSAGES_NOT_LOADED);
        }

        if (!userDatabase.isOpen()) {
            errors.add(Constants.ERROR_DATABASE_NOT_LOADED);
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "error";
        }

        return "welcome";
    }

}
