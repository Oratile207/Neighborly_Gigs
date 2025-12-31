package co.za.neighborlygigs.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterForm {

    @NotBlank(message = "Full name is required")
    private String firstName;

    @NotBlank(message = "Full name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @Pattern(regexp = "^\\+27\\d{9}$", message = "Phone must be in South African format: +27821234567")
    @NotBlank(message = "Phone number is required")
    private String phone;
}
