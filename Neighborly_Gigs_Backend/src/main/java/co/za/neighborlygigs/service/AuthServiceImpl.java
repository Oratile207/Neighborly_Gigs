package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.User;
import co.za.neighborlygigs.factory.UserFactory; // ✅ Import the class
import co.za.neighborlygigs.repository.UserRepository;
import co.za.neighborlygigs.repository.EmailVerificationTokenRepository;
import co.za.neighborlygigs.domain.EmailVerificationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository tokenRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           EmailVerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered");
        }

        User user = UserFactory.createRegularUser(email, password, firstName, lastName, phone, null, null, null);
        User savedUser = userRepository.save(user);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(savedUser);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(token);

        System.out.println("Verification email sent to: " + email + " | Token: " + token.getToken());
        return savedUser;
    }

    @Override
    public void sendVerificationEmail(String email) {
        throw new UnsupportedOperationException("Re-send email not implemented in MVP");
    }

    @Override
    public void verifyEmail(String tokenValue) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new RuntimeException("Verification token has expired");
        }

        User user = token.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        tokenRepository.delete(token);
    }
}