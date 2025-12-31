package co.za.neighborlygigs.factory;

import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.enums.TaskCategory;
import co.za.neighborlygigs.domain.enums.TaskStatus;
import co.za.neighborlygigs.domain.User;
import co.za.neighborlygigs.util.*;
import java.math.BigDecimal;

public class TaskFactory {
    public static Task createTask(String title, String description, TaskCategory category,
                                  BigDecimal budget, String address, String requirements, User poster) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (budget == null || budget.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Budget cannot be null or negative");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        if (poster == null) {
            throw new IllegalArgumentException("Poster cannot be null");
        }


        return Task.builder()
                .title(title)
                .description(description)
                .category(category)
                .budget(budget)
                .address(address)
                .requirements(requirements)
                .poster(poster)
                .status(TaskStatus.OPEN)
                .build();
    }

    public static Task createTaskWithLocation(String title, String description, TaskCategory category,
                                              BigDecimal budget, String address, String requirements, User poster) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (budget == null || budget.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Budget cannot be null or negative");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        if (poster == null) {
            throw new IllegalArgumentException("Poster cannot be null");
        }
        return Task.builder()
                .title(title)
                .description(description)
                .category(category)
                .budget(budget)
                .address(address)
                .requirements(requirements)
                .poster(poster)
                .status(TaskStatus.OPEN)
                .build();
    }

}
