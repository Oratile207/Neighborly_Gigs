package co.za.neighborlygigs.util;

public class ValidationUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        email = email.trim();
        if (email.isEmpty())
            return false;

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null)
            return false;
        phone = phone.trim();
        if (phone.isEmpty()) return false;


        String phoneRegex = "^(\\+?[0-9]{1,3})?([0-9]{10})$";
        return phone.matches(phoneRegex);
    }

    public static String generateId() {
        return java.util.UUID.randomUUID().toString();
    }

    private ValidationUtil() {}
}
