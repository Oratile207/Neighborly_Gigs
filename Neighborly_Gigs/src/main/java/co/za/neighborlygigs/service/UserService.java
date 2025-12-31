package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.User;
import org.springframework.web.multipart.MultipartFile;

// Extends generic CRUD + adds custom methods
public interface UserService {
    User read(String email);
    User updateProfile(String email, String bio, String phone);
    String uploadProfilePicture(String email, MultipartFile file);
    String uploadCv(String email, MultipartFile file);
}
