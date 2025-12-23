// co.za.neighborlygigs.dto.TaskForm.java

package co.za.neighborlygigs.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Data;
import co.za.neighborlygigs.domain.enums.TaskCategory;

@Data
public class TaskForm {

    @NotBlank(message = "Title is required")
    private String title; // e.g., "Lawn Mowing"

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private TaskCategory category; // CLEANING, YARD_WORK, PET_CARE, TUTORING, DELIVERY, ERRANDS

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "20.00", message = "Minimum budget is R20")
    private BigDecimal budget;

    @NotBlank(message = "Address is required")
    private String address;

    private String requirements;
}