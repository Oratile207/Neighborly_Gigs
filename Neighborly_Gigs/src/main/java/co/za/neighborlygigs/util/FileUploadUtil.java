package co.za.neighborlygigs.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUploadUtil {
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/jpg"
    );
    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" // .docx
    );

    public static boolean isImageFile(MultipartFile file) {
        return file != null && ALLOWED_IMAGE_TYPES.contains(file.getContentType());
    }

    public static boolean isDocumentFile(MultipartFile file) {
        return file != null && ALLOWED_DOCUMENT_TYPES.contains(file.getContentType());
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";
        int lastDot = fileName.lastIndexOf(".");
        return (lastDot > 0) ? fileName.substring(lastDot + 1).toLowerCase() : "";
    }

    private FileUploadUtil() {}
}
