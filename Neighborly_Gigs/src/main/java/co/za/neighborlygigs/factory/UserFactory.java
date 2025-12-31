package co.za.neighborlygigs.factory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import co.za.neighborlygigs.domain.User;
import co.za.neighborlygigs.domain.enums.Role;
import co.za.neighborlygigs.util.*;

public class UserFactory {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User createRegularUser(String email, String password, String firstName, String lastName, String phone, String bio, String profilePictureUrl, String cvUrl){
        if (ValidationUtil.isNullOrEmpty(firstName)) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (ValidationUtil.isNullOrEmpty(lastName)) {
            throw new IllegalArgumentException("Surname cannot be null or empty");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Email is invalid");
        }
        if (ValidationUtil.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Phone number is invalid");
        }
        String encodedPassword = passwordEncoder.encode(password);

        return User.builder()
                .email(email)
                .password(encodedPassword)
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.USER)
                .build();
    }

    public static User createAdminUser(String email, String password, String firstName, String lastName, String phone, Role role) {
        if (role != Role.ADMIN) {
            throw new IllegalArgumentException("Role must be ADMIN for admin user creation");
        }
        if (ValidationUtil.isNullOrEmpty(firstName) || ValidationUtil.isNullOrEmpty(lastName) || ValidationUtil.isNullOrEmpty(phone) || ValidationUtil.isNullOrEmpty(password) || ValidationUtil.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("All fields must be filled and not empty");
        }
        String encodedPassword = passwordEncoder.encode(password);

        return User.builder()
                .email(email)
                .password(encodedPassword)
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.ADMIN)
                .build();

    }
}
