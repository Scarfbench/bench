package quarkus.tutorial.async.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

@ApplicationScoped
public class MailSessionProducer {

    @Produces
    @ApplicationScoped
    Session mailSession() {
        Properties p = new Properties();
        p.put("mail.smtp.host", System.getProperty("quarkus.mailer.host", "localhost"));
        p.put("mail.smtp.port", System.getProperty("quarkus.mailer.port", "3025"));
        p.put("mail.smtp.auth", System.getProperty("quarkus.mailer.auth", "true"));
        p.put("mail.smtp.starttls.enable", System.getProperty("quarkus.mailer.start-tls", "false"));

        String user = System.getProperty("quarkus.mailer.username", "jack");
        String pass = System.getProperty("quarkus.mailer.password", "changeMe");

        return Session.getInstance(p, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
    }
}