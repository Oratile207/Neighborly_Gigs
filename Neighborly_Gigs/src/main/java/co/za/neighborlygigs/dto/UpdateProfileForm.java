package co.za.neighborlygigs.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateProfileForm {
    private String bio;

    @Pattern(regexp = "^\\+27\\d{9}$", message = "Phone must be in South African format: +27821234567")
    private String phone;
}
