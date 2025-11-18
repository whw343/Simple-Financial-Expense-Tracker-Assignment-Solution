/**
 * Visitor subclass representing a theme park guest (extends Person).
 * <p>Additional Attributes:
 * - visitorId: Unique visitor identifier (e.g., "VIS-001")
 * - membershipType: "VIP" or "Regular" (affects ride queue priority)
 * </p>
 * 
 * @author HD Developer
 * @version 1.0
 */
public class Visitor extends Person {
    private String visitorId;       // Visitor-specific ID (e.g., "VIS-001")
    private String membershipType;  // "VIP" or "Regular"

    /**
     * Default constructor (required by Part1).
     */
    public Visitor() {}

    /**
     * Parameterized constructor for Visitor.
     * @param id Generic person ID (e.g., "P001")
     * @param name Full name of visitor
     * @param age Age (0-120)
     * @param visitorId Visitor-specific ID (e.g., "VIS-001")
     * @param membershipType "VIP" or "Regular" (case-sensitive)
     * @throws IllegalArgumentException if age is invalid
     */
    public Visitor(String id, String name, int age, String visitorId, String membershipType) {
        super(id, name, age);
        this.visitorId = visitorId;
        this.membershipType = membershipType;
    }

    // ------------------------------ Getters & Setters ------------------------------
    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    /**
     * Implements abstract role method from Person.
     * @return String "Visitor"
     */
    @Override
    public String getRole() {
        return "Visitor";
    }

    /**
     * Returns a CSV-compatible string (dedicated for IO, avoids toString conflicts).
     * <p>Design Choice: Separates CSV serialization from display toString() to maintain flexibility.</p>
     * 
     * @return String in format: id,name,age,visitorId,membershipType
     */
    public String toCsvString() {
        return String.format("%s,%s,%d,%s,%s",
                getId(), getName(), getAge(), visitorId, membershipType);
    }

    /**
     * Parses a Visitor from a CSV line (static factory method).
     * <p>Validates CSV structure to ensure data integrity.</p>
     * 
     * @param csvLine Single line from CSV file
     * @return Visitor object parsed from CSV
     * @throws IllegalArgumentException if CSV format is invalid
     */
    public static Visitor fromCsv(String csvLine) {
        if (csvLine == null || csvLine.isBlank()) {
            throw new IllegalArgumentException("CSV line is null/blank");
        }

        String[] parts = csvLine.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Expected 5 columns (received " + parts.length + ")");
        }

        // Validate age is numeric
        int age;
        try {
            age = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid age: " + parts[2]);
        }

        return new Visitor(parts[0], parts[1], age, parts[2], parts[4]);
    }

    /**
     * Returns a human-readable string for display (not CSV).
     * @return Formatted string with visitor details
     */
    @Override
    public String toString() {
        return super.toString() + String.format(" | Visitor ID: %-6s | Membership: %-7s", visitorId, membershipType);
    }
}