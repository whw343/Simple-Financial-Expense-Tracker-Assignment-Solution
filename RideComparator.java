import java.util.Comparator;

/**
 * Comparator for sorting Visitor objects with multi-level rules.
 * <p>Sorting Priority:
 * 1. Membership Type (VIP > Regular)
 * 2. Age (Descending: older first)
 * 3. Name (Ascending: alphabetical) [tiebreaker for identical age/membership]
 * </p>
 * <p>Design Rationale: Ensures deterministic sorting even when visitors have
 * identical membership and age, preventing unstable ordering.</p>
 * 
 * @author HD Developer
 * @version 1.0
 */
public class RideComparator implements Comparator<Visitor> {

    /**
     * Compares two Visitor objects using hierarchical rules.
     * @param v1 First Visitor to compare
     * @param v2 Second Visitor to compare
     * @return int: 
     *         - Negative if v1 should come before v2
     *         - Positive if v1 should come after v2
     *         - Zero if identical (unlikely with unique IDs)
     * @throws NullPointerException if either visitor is null
     */
    @Override
    public int compare(Visitor v1, Visitor v2) {
        // Null safety (HD-level robustness)
        if (v1 == null && v2 == null) return 0;
        if (v1 == null) return 1; // Nulls go last
        if (v2 == null) return -1;

        // 1. Compare membership type (VIP > Regular)
        int membershipCompare = v2.getMembershipType().compareTo(v1.getMembershipType());
        if (membershipCompare != 0) {
            return membershipCompare;
        }

        // 2. Compare age (descending)
        int ageCompare = Integer.compare(v2.getAge(), v1.getAge());
        if (ageCompare != 0) {
            return ageCompare;
        }

        // 3. Tiebreaker: Compare name (ascending alphabetical)
        return v1.getName().compareToIgnoreCase(v2.getName());
    }
}