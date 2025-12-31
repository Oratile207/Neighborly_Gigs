package co.za.neighborlygigs.util;

public class GeoCodingUtil {

    /**
     * Cleans and formats address for geocoding API
     * Example: "123 Main St, Pretoria" → "123 Main St, Pretoria, South Africa"
     */
    public static String formatAddressForGeocoding(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        String cleaned = address.trim();
        if (!cleaned.toLowerCase().contains("south africa") && !cleaned.toLowerCase().contains("za")) {
            cleaned += ", South Africa";
        }
        return cleaned;
    }

    private GeoCodingUtil() {}
}