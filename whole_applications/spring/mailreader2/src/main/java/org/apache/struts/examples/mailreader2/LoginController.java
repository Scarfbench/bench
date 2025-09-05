package org.apache.struts.examples.mailreader2;

import java.util.Locale;
import org.apache.struts.examples.mailreader2.dao.ExpiredPasswordException;
import org.apache.struts.examples.mailreader2.dao.User;
import org.apache.struts.examples.mailreader2.dto.LoginForm;
import org.apache.struts.examples.mailreader2.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;

    public LoginController(UserService userService, MessageSource messageSource,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult result,
            Locale locale,
            Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "login";
        }
        try {
            User user = userService.findUser(form.getUsername(), form.getPassword());

            if (user == null) {
                String message = messageSource.getMessage("error.password.mismatch", null,
                        "error.password.mismatch", locale);

                result.rejectValue(Constants.PASSWORD_MISMATCH_FIELD, "error.password.mismatch",
                        message);
                return "login";
            }

            var auth = new UsernamePasswordAuthenticationToken(
                    form.getUsername(), form.getPassword());
            var authenticated = authenticationManager.authenticate(auth);

            SecurityContextHolder.getContext().setAuthentication(authenticated);

            // persist across session
            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            return "redirect:/mainMenu";
        } catch (ExpiredPasswordException e) {
            return "changePassword";
        }
    }

    @PostMapping("/login/cancel")
    public String cancelLogin() {
        return "redirect:/welcome";
    }
}
