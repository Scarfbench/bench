package org.apache.struts.examples.mailreader2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.apache.struts.examples.mailreader2.dao.User;
import org.apache.struts.examples.mailreader2.dto.RegistrationForm;
import org.apache.struts.examples.mailreader2.dto.SubscriptionDto;
import org.apache.struts.examples.mailreader2.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final InMemoryUserDetailsManager userDetailsManager;

    public RegistrationController(UserService userService,
            AuthenticationManager authenticationManager,
            InMemoryUserDetailsManager userDetailsManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsManager = userDetailsManager;
    }

    @GetMapping
    public String showCreateForm(Model model) {
        User user = userService.getAuthUser();
        if (user == null) {
            RegistrationForm registrationForm = new RegistrationForm();
            registrationForm.setTask("Create");
            model.addAttribute("registrationForm", registrationForm);
        } else {
            RegistrationForm form = RegistrationForm.fromUser(user);
            form.setTask("Edit");
            model.addAttribute("registrationForm", form);
        }

        return "registration";
    }

    @PostMapping
    public String saveRegistration(
            @Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
            BindingResult result,
            Model model, HttpServletRequest request) {
        // ugly fix: repopulate subscriptions since they're not form submitted
        populateSubscriptions(registrationForm);

        if (result.hasErrors()) {
            return "registration";
        }

        boolean isCreate =
                registrationForm.getTask().equals("Create") && userService.getAuthUser() == null;

        if (isCreate) {
            if (userService.exists(registrationForm.getUsername())) {
                result.reject("error.username.unique");
                return "registration";
            }

            User user = userService.create(registrationForm);
            addUser(user);
            authenticate(user, request);
        } else {
            User user = userService.update(registrationForm);
            addUser(user);
        }

        return isCreate ? "redirect:/mainMenu" : "registration";
    }

    private void authenticate(User user, HttpServletRequest request) {
        var auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());
        var authenticated = authenticationManager.authenticate(auth);

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        // persist across session
        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
    }

    private void addUser(User user) {
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password("{noop}" + user.getPassword())
                .authorities("ROLE_user").build();
        if (!userDetailsManager.userExists(user.getUsername())) {
            userDetailsManager.createUser(ud);
        } else {
            userDetailsManager.updateUser(ud);
        }
    }

    private void populateSubscriptions(RegistrationForm registrationForm) {
        User user = userService.getAuthUser();
        if (user != null) {
            var subscriptions = Arrays.stream(user.getSubscriptions()).map(SubscriptionDto::from)
                    .toArray(SubscriptionDto[]::new);
            registrationForm.setSubscriptions(subscriptions);
        }
    }
}

