package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.User;
import co.za.neighborlygigs.domain.enums.Role;
import co.za.neighborlygigs.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        sendWelcomeEmail(savedUser.getEmail());
        return savedUser;
    }


    private void sendWelcomeEmail(String to) {
        try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Welcome to Neighborly Gigs!");
        helper.setText(
                "<h2>Hello!</h2>" +
                        "<p>Your Neighborly Gigs account has been created successfully.</p>" +
                        "<p>Please click the link below to login</p>" +
                        "<a href='http://localhost:8080/login" + "' " +
                        "style='background-color: #49c1f0; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>" +
                        "Login</a>",
                true
        );

        mailSender.send(message);
    } catch (Exception e) {
        System.err.println("Failed to send welcome email to " + e.getMessage());

    }


//    @Override
//    public void sendVerificationEmail(String email) {
//        throw new UnsupportedOperationException("Re-send email not implemented in MVP");
//    }

    }
}