package co.za.neighborlygigs.controller;

import co.za.neighborlygigs.domain.User;
import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.dto.*;
import co.za.neighborlygigs.security.CustomUserDetails;
import co.za.neighborlygigs.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private final AuthService authService;
    private final UserService userService;
    private final TaskService taskService;
    private final ApplicationService applicationService;
    private final ReviewService reviewService;

    public WebController(AuthService authService,
                         UserService userService,
                         TaskService taskService,
                         ApplicationService applicationService,
                         ReviewService reviewService) {
        this.authService = authService;
        this.userService = userService;
        this.taskService = taskService;
        this.applicationService = applicationService;
        this.reviewService = reviewService;
    }

    // LANDING PAGE
    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    // LOGIN PAGE - ✅ FIXED: No user loading
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Model model
    ) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out successfully.");
        if (registered != null) model.addAttribute("message", "Registration successful! Please log in.");
        // ❌ DO NOT load user here - Spring Security handles authentication
        return "login";
    }

    // SIGN UP PAGE
    @GetMapping("/register")
    public String registerForm(Model model) {
        System.out.println("✅ REGISTER PAGE LOADED");
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registerForm", form);
            return "register";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("passwordMatchError", "Passwords do not match");
            model.addAttribute("registerForm", form);
            return "register";
        }

        try {
            authService.registerUser(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getEmail(),
                    form.getPassword(),
                    form.getPhone()
            );
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerForm", form);
            return "register";
        }
    }

    // ✅ DASHBOARD: Homepage for logged-in users
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        List<Task> openTasks = taskService.getAllOpenTasks();
        model.addAttribute("tasks", openTasks);

        Map<String, Object> stats = new HashMap<>();
        stats.put("jobsPosted", 1200);
        stats.put("activeWorkers", 850);
        stats.put("avgRating", 4.8);
        stats.put("successRate", 98);
        model.addAttribute("stats", stats);
        model.addAttribute("searchQuery", "");

        return "dashboard";
    }

    // JOB LIST PAGE (BROWSE JOBS)
    @GetMapping("/tasks")
    public String jobList(Model model) {
        List<Task> openTasks = taskService.getAllOpenTasks();
        model.addAttribute("jobs", openTasks);
        model.addAttribute("searchQuery", "");
        return "jobs";
    }

    // CREATE TASK PAGE
    @GetMapping("/tasks/new")
    public String createTaskForm(Model model) {
        model.addAttribute("taskForm", new TaskForm());
        return "create-task";
    }

    @PostMapping("/tasks")
    public String createTask(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid TaskForm form,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("taskForm", form);
            return "create-task";
        }

        try {
            taskService.createTask(
                    form.getTitle(),
                    form.getDescription(),
                    form.getCategory(),
                    form.getBudget(),
                    form.getAddress(),
                    form.getRequirements(),
                    currentUser.getEmail()
            );
            return "redirect:/dashboard?taskPosted";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create task. Please try again.");
            model.addAttribute("taskForm", form);
            return "create-task";
        }
    }

    // JOB DETAIL PAGE
    @GetMapping("/tasks/{id}")
    public String taskDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            Model model
    ) {
        Task task = taskService.getTaskById(id);
        boolean isOwner = currentUser != null && task.getPoster().getEmail().equals(currentUser.getEmail());
        model.addAttribute("task", task);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("user", currentUser != null);
        return "task-detail";
    }

    // APPLY TO TASK
    @PostMapping("/applications")
    public String applyToTask(
            @RequestParam Long taskId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            applicationService.applyToTask(taskId, currentUser.getEmail(), null);
            redirectAttributes.addFlashAttribute("message", "Application sent successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tasks/" + taskId;
    }

    // PROFILE PAGES
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = userService.read(currentUser.getEmail());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = userService.read(currentUser.getEmail());
        model.addAttribute("user", user);
        model.addAttribute("updateProfileForm", new UpdateProfileForm());
        return "profile-edit";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid UpdateProfileForm form,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            User user = userService.read(currentUser.getEmail());
            model.addAttribute("user", user);
            model.addAttribute("updateProfileForm", form);
            return "profile";
        }

        try {
            userService.updateProfile(currentUser.getEmail(), form.getBio(), form.getPhone());
            return "redirect:/profile?updated";
        } catch (Exception e) {
            User user = userService.read(currentUser.getEmail());
            model.addAttribute("user", user);
            model.addAttribute("updateProfileForm", form);
            model.addAttribute("error", "Failed to update profile.");
            return "profile";
        }
    }

    @PostMapping("/profile/picture")
    public String uploadProfilePicture(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.uploadProfilePicture(currentUser.getEmail(), file);
            redirectAttributes.addFlashAttribute("message", "Profile picture updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/cv")
    public String uploadCv(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.uploadCv(currentUser.getEmail(), file);
            redirectAttributes.addFlashAttribute("message", "CV uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect://profile";
    }

    // SUBMIT REVIEW
    @PostMapping("/reviews")
    public String submitReview(
            @RequestParam Long taskId,
            @RequestParam String revieweeEmail,
            @RequestParam int rating,
            @RequestParam(required = false) String comment,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            RedirectAttributes redirectAttributes
    ) {
        try {
            reviewService.submitReview(taskId, currentUser.getEmail(), revieweeEmail, rating, comment);
            redirectAttributes.addFlashAttribute("message", "Thank you for your review!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/tasks/" + taskId;
    }

    // LOGOUT
    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}