/**
 * Abstract base class representing a generic person in the theme park system.
 * <p>Design Rationale: Serves as a template for Employee and Visitor subclasses to enforce
 * consistent attributes (ID, name, age) while allowing role-specific extensions.
 * Cannot be instantiated to prevent misuse of generic "Person" objects.</p>
 * 
 * @author HD Developer
 * @version 1.0
 */
public abstract class Person {
    private String id;       // Unique identifier (e.g., "P001")
    private String name;     // Full name of the person
    private int age;         // Age (validated between 0-120)

    /**
     * Default constructor (required by Part1).
     * Initializes empty values for subclasses to populate.
     */
    public Person() {}

    /**
     * Parameterized constructor with validation.
     * @param id Unique ID string (non-null)
     * @param name Full name (non-null)
     * @param age Age (0-120 only)
     * @throws IllegalArgumentException if age is invalid
     */
    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        setAge(age); // Enforce age validation via setter
    }

    // ------------------------------ Getters & Setters ------------------------------
    /**
     * Gets the unique ID of the person.
     * @return String ID (e.g., "P001")
     */
    public String getId() { return id; }

    /**
     * Sets the unique ID of the person.
     * @param id Non-null string identifier
     */
    public void setId(String id) { this.id = id; }

    /**
     * Gets the full name of the person.
     * @return String name
     */
    public String getName() { return name; }

    /**
     * Sets the full name of the person.
     * @param name Non-null string name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the age of the person (validated).
     * @return int age (0-120)
     */
    public int getAge() { return age; }

    /**
     * Sets the age with strict validation (0-120).
     * <p>Design Choice: Prevents invalid age values (e.g., negative or unrealistic ages)
     * to maintain data integrity in the system.</p>
     * 
     * @param age Age value to set
     * @throws IllegalArgumentException if age < 0 or > 120
     */
    public void setAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 0 and 120 (received: " + age + ")");
        }
        this.age = age;
    }

    /**
     * Abstract method to return the role of the person (Employee/Visitor).
     * <p>Design Rationale: Forces subclasses to explicitly define their role,
     * enabling type-specific behavior in the system.</p>
     * 
     * @return String role (e.g., "Employee" or "Visitor")
     */
    public abstract String getRole();

    /**
     * Returns a formatted string representation of the person.
     * @return String with ID, name, and age
     */
    @Override
    public String toString() {
        return String.format("ID: %-5s | Name: %-10s | Age: %d", id, name, age);
    }
}