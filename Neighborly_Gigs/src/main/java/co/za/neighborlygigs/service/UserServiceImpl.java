// co.za.neighborlygigs.service.UserServiceImpl.java

package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.User;
import co.za.neighborlygigs.repository.UserRepository;
import co.za.neighborlygigs.util.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User read(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public User updateProfile(String email, String bio, String phone) {
        User user = read(email);
        user.setBio(bio);
        user.setPhone(phone);
        return userRepository.save(user);
    }

    @Override
    public String uploadProfilePicture(String email, MultipartFile file) {
        if (!FileUploadUtil.isImageFile(file)) {
            throw new RuntimeException("Only image files (JPEG/PNG) are allowed");
        }
        // TODO: Save to cloud storage (AWS S3, etc.)
        return "https://neighborlygigs.co.za/images/" + email + "/profile.jpg";
    }

    @Override
    public String uploadCv(String email, MultipartFile file) {
        if (!FileUploadUtil.isDocumentFile(file)) {
            throw new RuntimeException("Only PDF or DOC files are allowed");
        }
        return "https://neighborlygigs.co.za/cvs/" + email + "/cv.pdf";
    }
}