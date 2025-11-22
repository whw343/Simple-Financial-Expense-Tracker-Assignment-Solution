import java.util.*;

/**
 * Core Ride class implementing RideInterface (manages queue, history, and operations).
 * <p>Key Design Choices:
 * - Queue: LinkedList (optimal for FIFO operations with O(1) add/remove)
 * - History: LinkedList (supports efficient iteration and sorting)
 * - Cycle Logic: Limits riders per cycle to maxRidersPerCycle for safety
 * </p>
 * 
 * @author HD Developer
 * @version 1.0
 */
public class Ride implements RideInterface {
    private String rideId;                  // Unique ride ID (e.g., "R001")
    private String rideName;                // Ride name (e.g., "Thunderbolt")
    private Employee operator;              // Assigned operator (required for operation)
    private Queue<Visitor> waitingQueue;    // FIFO queue for waiting visitors (Part3)
    private LinkedList<Visitor> rideHistory;// Historical riders (Part4A)
    private int maxRidersPerCycle;          // Max riders per cycle (safety constraint)
    private int cycleCount;                 // Number of cycles completed

    /**
     * Parameterized constructor for Ride (initializes collections).
     * <p>Design Rationale: Initializes queues/history in constructor to avoid null pointers.</p>
     * 
     * @param rideId Unique ride ID
     * @param rideName Descriptive ride name
     * @param operator Assigned employee (can be null initially)
     * @param maxRidersPerCycle Max riders per cycle (positive integer)
     */
    public Ride(String rideId, String rideName, Employee operator, int maxRidersPerCycle) {
        this.rideId = rideId;
        this.rideName = rideName;
        this.operator = operator;
        this.maxRidersPerCycle = maxRidersPerCycle;
        this.cycleCount = 0;
        this.waitingQueue = new LinkedList<>(); // LinkedList for Queue (FIFO)
        this.rideHistory = new LinkedList<>();  // LinkedList for history (sortable)
    }

    // ------------------------------ Getters & Setters ------------------------------
    public String getRideId() { return rideId; }
    public String getRideName() { return rideName; }
    public Employee getOperator() { return operator; }
    public void setOperator(Employee operator) { this.operator = operator; }
    public int getMaxRidersPerCycle() { return maxRidersPerCycle; }
    public int getCycleCount() { return cycleCount; }
    public LinkedList<Visitor> getRideHistory() { return rideHistory; }

    // ------------------------------ Part3: Queue Operations ------------------------------
    /**
     * Adds a visitor to the waiting queue (FIFO).
     * <p>Handles null visitors gracefully to prevent crashes.</p>
     * 
     * @param visitor Visitor to add (can be null)
     */
    @Override
    public void addToQueue(Visitor visitor) {
        if (visitor == null) {
            System.err.println("[ERROR] Cannot add null visitor to queue (" + rideName + ")");
            return;
        }
        waitingQueue.offer(visitor);
        System.out.println("[QUEUE] Added " + visitor.getName() + " to " + rideName + " queue");
    }

    /**
     * Removes a specific visitor from the queue (not just the head).
     * <p>Note: Inefficient for large queues (O(n)), but required by Part3.</p>
     * 
     * @param visitor Visitor to remove
     * @return true if removed, false otherwise
     */
    @Override
    public boolean removeFromQueue(Visitor visitor) {
        if (visitor == null) {
            System.err.println("[ERROR] Cannot remove null visitor from queue (" + rideName + ")");
            return false;
        }
        boolean removed = waitingQueue.remove(visitor);
        if (removed) {
            System.out.println("[QUEUE] Removed " + visitor.getName() + " from " + rideName + " queue");
        } else {
            System.err.println("[ERROR] " + visitor.getName() + " not found in " + rideName + " queue");
        }
        return removed;
    }

    /**
     * Prints the waiting queue with numbered entries (user-friendly).
     */
    @Override
    public void printQueue() {
        System.out.println("\n[QUEUE] " + rideName + " Waiting Queue (" + waitingQueue.size() + " visitors):");
        if (waitingQueue.isEmpty()) {
            System.out.println("  (Empty)");
            return;
        }
        int position = 1;
        for (Visitor v : waitingQueue) {
            System.out.println("  " + position++ + ". " + v);
        }
    }

    // ------------------------------ Part4A: History Operations ------------------------------
    /**
     * Adds a visitor to the ride history (permanent record).
     * @param visitor Visitor to add (non-null)
     */
    @Override
    public void addToHistory(Visitor visitor) {
        if (visitor == null) {
            System.err.println("[ERROR] Cannot add null visitor to history (" + rideName + ")");
            return;
        }
        rideHistory.add(visitor);
        System.out.println("[HISTORY] Added " + visitor.getName() + " to " + rideName + " history");
    }

    /**
     * Checks if a visitor has ridden the ride before.
     * @param visitor Visitor to check
     * @return true if in history, false otherwise
     */
    @Override
    public boolean hasRidden(Visitor visitor) {
        if (visitor == null) {
            System.err.println("[ERROR] Cannot check null visitor in history (" + rideName + ")");
            return false;
        }
        boolean exists = rideHistory.contains(visitor);
        System.out.println("[HISTORY] " + visitor.getName() + " has ridden " + rideName + ": " + exists);
        return exists;
    }

    /**
     * Returns the count of visitors in the history (for statistics).
     * @return int number of visitors
     */
    @Override
    public int getHistoryCount() {
        System.out.println("[HISTORY] " + rideName + " total riders: " + rideHistory.size());
        return rideHistory.size();
    }

    /**
     * Prints the ride history using Iterator (Part4A requirement).
     * <p>Design Note: Uses Iterator to comply with assignment requirements,
     * even though enhanced for-loop is more concise.</p>
     */
    @Override
    public void printHistory() {
        System.out.println("\n[HISTORY] " + rideName + " Ride History (" + rideHistory.size() + " visitors):");
        if (rideHistory.isEmpty()) {
            System.out.println("  (Empty)");
            return;
        }
        Iterator<Visitor> iterator = rideHistory.iterator();
        int entry = 1;
        while (iterator.hasNext()) {
            System.out.println("  " + entry++ + ". " + iterator.next());
        }
    }

    // ------------------------------ Part4B: Sort History ------------------------------
    /**
     * Sorts the ride history using RideComparator (multi-level rules).
     * <p>Design Rationale: Uses external Comparator (not Comparable) to comply with Part4B.</p>
     */
    public void sortHistory() {
        if (rideHistory.isEmpty()) {
            System.err.println("[WARNING] Cannot sort empty history (" + rideName + ")");
            return;
        }
        Collections.sort(rideHistory, new RideComparator());
        System.out.println("[HISTORY] Sorted " + rideName + " history by VIP → Age → Name");
    }

    // ------------------------------ Part5: Run Cycle ------------------------------
    /**
     * Runs one operational cycle of the ride (transfers visitors from queue to history).
     * <p>Business Rules:
     * 1. Requires an assigned operator
     * 2. Requires at least one visitor in the queue
     * 3. Limits riders to maxRidersPerCycle per cycle
     * </p>
     */
    @Override
    public void runCycle() {
        System.out.println("\n[CYCLE] Attempting to run " + rideName + " Cycle " + (cycleCount + 1));
        
        // Validate prerequisites
        if (operator == null) {
            System.err.println("[CYCLE FAILED] No operator assigned to " + rideName);
            return;
        }
        if (waitingQueue.isEmpty()) {
            System.err.println("[CYCLE FAILED] No visitors in " + rideName + " queue");
            return;
        }

        // Calculate number of riders (up to max per cycle)
        int ridersThisCycle = Math.min(maxRidersPerCycle, waitingQueue.size());
        System.out.println("[CYCLE] " + rideName + " loading " + ridersThisCycle + " riders...");

        // Transfer visitors from queue to history
        for (int i = 0; i < ridersThisCycle; i++) {
            Visitor rider = waitingQueue.poll(); // Remove from queue head (FIFO)
            addToHistory(rider); // Add to permanent history
        }

        // Increment cycle count
        cycleCount++;
        System.out.println("[CYCLE SUCCESS] " + rideName + " completed Cycle " + cycleCount);
    }
}