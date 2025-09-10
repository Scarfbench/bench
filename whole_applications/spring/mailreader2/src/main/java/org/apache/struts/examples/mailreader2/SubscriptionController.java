package org.apache.struts.examples.mailreader2;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.struts.examples.mailreader2.dao.Subscription;
import org.apache.struts.examples.mailreader2.dao.User;
import org.apache.struts.examples.mailreader2.dto.SubscriptionForm;
import org.apache.struts.examples.mailreader2.exceptions.NoAuthUserException;
import org.apache.struts.examples.mailreader2.service.SubscriptionService;
import org.apache.struts.examples.mailreader2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public SubscriptionController(SubscriptionService subscriptionService,
            UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @ModelAttribute("types")
    public Map<String, String> getTypes() {
        Map<String, String> types = new LinkedHashMap<>();
        types.put("imap", "IMAP Protocol");
        types.put("pop3", "POP3 Protocol");
        return types;
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        SubscriptionForm form = new SubscriptionForm();
        User user = userService.getAuthUser();
        if (user == null) {
            throw new NoAuthUserException();
        }
        form.setTask("Create");
        form.setUser(user.getUsername());
        model.addAttribute("subscriptionForm", form);
        return "subscription";
    }

    @GetMapping("/edit/{host}")
    public String editForm(@PathVariable String host, Model model) {
        Subscription subscription = subscriptionService.getSubscription(host);
        if (subscription == null) {
            return "error";
        }
        SubscriptionForm form = SubscriptionForm.from(subscription);
        form.setTask("Edit");
        model.addAttribute("subscriptionForm", form);
        return "subscription";
    }

    @GetMapping("/delete/{host}")
    public String deleteForm(@PathVariable String host, Model model) {
        Subscription subscription = subscriptionService.getSubscription(host);
        if (subscription == null) {
            return "error";
        }
        SubscriptionForm form = SubscriptionForm.from(subscription);
        form.setTask("Delete");
        model.addAttribute("subscriptionForm", form);
        return "subscription";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("subscriptionForm") SubscriptionForm form,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "subscription";
        }

        if (form.getTask().equals("Delete")) {
            subscriptionService.delete(form.getHost());
        } else if (form.getTask().equals("Create")) {
            if (subscriptionService.exists(form.getHost())) {
                result.rejectValue(Constants.HOST, "That hostname is already defined",
                        "That hostname is already defined");
                return "subscription";
            }
            subscriptionService.create(form);
        } else {
            subscriptionService.update(form);
        }

        return "redirect:/registration";
    }

}

