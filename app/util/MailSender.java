package util;

import play.Configuration;
import play.Logger;
import play.api.PlayConfig;
import play.api.PlayConfig$;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Par Alexis le 06/11/2016.
 */

public class MailSender implements Runnable
{

    private static final String USERNAME = "fr.jobid.mail@gmail.com";
    private static final String PASSWORD = "3[Tsf5K^7,aC";
    private static final Properties PROPERTIES = System.getProperties();

    static {
        PROPERTIES.setProperty("mail.smtp.auth", "true");
        PROPERTIES.setProperty("mail.smtp.starttls.enable", "true");
        PROPERTIES.setProperty("mail.smtp.host", "smtp.gmail.com");
        PROPERTIES.setProperty("mail.smtp.port", "587");
    }

    String email;
    String subject;
    String htmlContent;

    public MailSender(String email, String subject, String htmlContent)
    {
        this.email = email;
        this.subject = subject;
        this.htmlContent = htmlContent;
    }

    public void sendEmail()
    {
        new Thread(this).start();
    }

    private void send()
    {
        Session session = Session.getInstance(PROPERTIES,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@jobid.fr"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            Logger.info("Email sent to " + email + " at " + System.currentTimeMillis() + " reason " + subject);

        } catch (MessagingException e) {
            Logger.error(e.getMessage());
        }
    }

    @Override
    public void run()
    {
        send();
    }
}