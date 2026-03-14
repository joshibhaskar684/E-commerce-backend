package app.auth.service.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailServices {
    private JavaMailSender javaMailSender;

    public EmailServices(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }












    public void sendOtpEmail(String toEmail, String otp) throws Exception {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(new InternetAddress("joshibhaskar"));
        helper.setTo(toEmail);
        helper.setSubject("OTP Verification Required");

        String emailBody = String.format(
                "Hello,\n\n" +
                        "We received a request to verify your email address.\n\n" +
                        "Your One-Time Password (OTP) is:\n\n" +
                        "🔐  %s\n\n" +
                        "This OTP is valid for the next 2 minutes.\n" +
                        "For security reasons, please do not share this code with anyone.\n\n" +
                        "If you did not request this verification, you can safely ignore this email.\n\n" +
                        "Thank you,\n"

        );

        helper.setText(emailBody, false); // false = plain text
        javaMailSender.send(message);
    }











}
