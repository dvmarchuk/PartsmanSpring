package com.example.partsmanspring.services;

import com.example.partsmanspring.dao.AuthDAO;
import com.example.partsmanspring.models.AuthToken;
import com.example.partsmanspring.models.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@Configuration
@AllArgsConstructor
public class MailService {

        private JavaMailSender javaMailSender;
//        private AuthToken authToken;

//    public MailService(AuthToken authToken) {
//        this.authToken = authToken;
//    }

    public void send(User user) {
                //mime message can hold all different kinds of data to send
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();

                //but we need to put it into MimeMessageHelper to open the extra options like setTo that we use below
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
                try {
                    mimeMessage.setFrom(new InternetAddress("marchukandtheworld@gmail.com"));
                    helper.setTo(user.getEmail());
                            helper.setText("<a href=http://localhost:8080/activate/"+ user.getToken() +">Confirm your account</a>", true);//with html true we can write in HTML
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                javaMailSender.send(mimeMessage);

            }

            //dont forget to add to pom
}
