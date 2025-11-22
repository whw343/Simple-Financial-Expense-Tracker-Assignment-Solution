/**
 * Main class to demonstrate all PRVMS features with comprehensive test cases.
 * <p>Test Coverage:
 * - Basic functionality (queue, history, cycles)
 * - Boundary conditions (empty queue, null inputs, invalid files)
 * - Error handling (invalid age, malformed CSV)
 * </p>
 * 
 * @author HD Developer
 * @version 1.0
 */
public class AssignmentTwo {
    public static void main(String[] args) {
        System.out.println("==================================== PROG2004 A2 - ENHANCED TEST SUITE ====================================");
        
        // ------------------------------ Setup Test Data ------------------------------
        Employee operator = new Employee("E001", "John Smith", 38, "EMP-001", "Roller Coaster Operator");
        Employee nullOperator = null; // For testing no-operator scenario
        
        // Valid visitors (including duplicates for sorting tiebreaker)
        Visitor v1 = new Visitor("V001", "Alice", 25, "VIS-001", "VIP");
        Visitor v2 = new Visitor("V002", "Bob", 19, "VIS-002", "Regular");
        Visitor v3 = new Visitor("V003", "Charlie", 30, "VIS-003", "VIP");
        Visitor v4 = new Visitor("V004", "Diana", 22, "VIS-004", "Regular");
        Visitor v5 = new Visitor("V005", "Ethan", 30, "VIS-005", "VIP"); // Same age/membership as v3
        Visitor v6 = new Visitor("V006", "Fiona", 28, "VIS-006", "Regular");
        Visitor v7 = new Visitor("V007", "George", 40, "VIS-007", "VIP");
        Visitor nullVisitor = null; // For null input testing
        
        // Invalid visitor (age out of range)
        Visitor invalidAgeVisitor = new Visitor();
        try {
            invalidAgeVisitor.setAge(150); // Should throw exception
        } catch (IllegalArgumentException e) {
            System.err.println("\n[EXPECTED ERROR] " + e.getMessage());
        }

        // ------------------------------ Test 1: No Operator (Boundary Case) ------------------------------
        System.out.println("\n==================================== TEST 1: NO OPERATOR ====================================");
        Ride noOperatorRide = new Ride("R002", "Broken Coaster", nullOperator, 3);
        noOperatorRide.addToQueue(v1);
        noOperatorRide.runCycle(); // Should fail (no operator)

        // ------------------------------ Test 2: Empty Queue (Boundary Case) ------------------------------
        System.out.println("\n==================================== TEST 2: EMPTY QUEUE ====================================");
        Ride emptyQueueRide = new Ride("R003", "Empty Coaster", operator, 3);
        emptyQueueRide.runCycle(); // Should fail (empty queue)

        // ------------------------------ Test 3: Null Visitor Input ------------------------------
        System.out.println("\n==================================== TEST 3: NULL VISITOR INPUT ====================================");
        Ride nullInputRide = new Ride("R004", "Null Test Coaster", operator, 3);
        nullInputRide.addToQueue(nullVisitor); // Should handle gracefully
        nullInputRide.addToHistory(nullVisitor); // Should handle gracefully

        // ------------------------------ Test 4: Sorting Tiebreaker (Same Age/Membership) ------------------------------
        System.out.println("\n==================================== TEST 4: SORTING TIEBREAKER ====================================");
        Ride sortingRide = new Ride("R005", "Sort Test Coaster", operator, 3);
        sortingRide.addToHistory(v1);
        sortingRide.addToHistory(v3);
        sortingRide.addToHistory(v5); // Same age/membership as v3
        sortingRide.addToHistory(v7);
        System.out.println("\nBefore sorting (tie between Charlie/Ethan):");
        sortingRide.printHistory();
        sortingRide.sortHistory();
        System.out.println("\nAfter sorting (tiebreaker: name):");
        sortingRide.printHistory();

        // ------------------------------ Test 5: Invalid CSV Import ------------------------------
        System.out.println("\n==================================== TEST 5: INVALID CSV IMPORT ====================================");
        Utils.importHistory("invalid_file.csv"); // Non-existent file
        Utils.importHistory(""); // Blank path

        // ------------------------------ Test 6: Empty History Export ------------------------------
        System.out.println("\n==================================== TEST 6: EMPTY HISTORY EXPORT ====================================");
        Ride emptyHistoryRide = new Ride("R006", "Empty History Coaster", operator, 3);
        Utils.exportHistory(emptyHistoryRide.getRideHistory(), "empty_history.csv"); // Should skip

        // ------------------------------ Test 7: Full End-to-End Test ------------------------------
        System.out.println("\n==================================== TEST 7: END-TO-END WORKFLOW ====================================");
        Ride thunderbolt = new Ride("R001", "Thunderbolt", operator, 3);
        
        // Add to queue
        thunderbolt.addToQueue(v1);
        thunderbolt.addToQueue(v2);
        thunderbolt.addToQueue(v3);
        thunderbolt.addToQueue(v4);
        thunderbolt.addToQueue(v5);
        
        // Run cycle
        thunderbolt.printQueue();
        thunderbolt.runCycle();
        
        // Export/import
        String csvPath = "thunderbolt_history.csv";
        Utils.exportHistory(thunderbolt.getRideHistory(), csvPath);
        Ride importedRide = new Ride("R007", "Imported Thunderbolt", operator, 3);
        importedRide.getRideHistory().addAll(Utils.importHistory(csvPath));
        importedRide.printHistory();

        System.out.println("\n==================================== TEST SUITE COMPLETE ====================================");
    }
}