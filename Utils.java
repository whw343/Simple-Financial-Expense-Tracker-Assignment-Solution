import java.io.*;
import java.util.LinkedList;

/**
 * Utility class for CSV file operations (export/import of ride history).
 * <p>Design Goals:
 * - Ensure data integrity during IO operations
 * - Provide clear error messages for debugging
 * - Handle edge cases (empty files, invalid paths, malformed CSV)
 * </p>
 * 
 * @author HD Developer
 * @version 1.0
 */
public class Utils {

    /**
     * Exports ride history to a CSV file with a header row.
     * <p>CSV Format: id,name,age,visitorId,membershipType</p>
     * 
     * @param history LinkedList of Visitor objects to export (non-null)
     * @param filePath Path to the output CSV file (non-blank)
     * @return true if export succeeds, false otherwise
     */
    public static boolean exportHistory(LinkedList<Visitor> history, String filePath) {
        // Validate inputs (HD-level defensive programming)
        if (history == null) {
            System.err.println("[ERROR] Export failed: History list is null");
            return false;
        }
        if (history.isEmpty()) {
            System.err.println("[WARNING] Export skipped: History list is empty");
            return false;
        }
        if (filePath == null || filePath.isBlank()) {
            System.err.println("[ERROR] Export failed: File path is invalid/blank");
            return false;
        }

        // Use try-with-resources to auto-close streams (resource safety)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header (improves CSV readability)
            writer.write("id,name,age,visitorId,membershipType");
            writer.newLine();

            // Write each visitor in CSV format
            for (Visitor visitor : history) {
                writer.write(visitor.toCsvString()); // Use dedicated CSV method (not toString)
                writer.newLine();
            }

            System.out.println("[SUCCESS] Exported " + history.size() + " visitors to: " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("[ERROR] Export failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Imports ride history from a CSV file (with header row).
     * <p>Handles malformed lines gracefully by skipping them.</p>
     * 
     * @param filePath Path to the input CSV file (non-blank)
     * @return LinkedList of Visitor objects (empty if import fails)
     */
    public static LinkedList<Visitor> importHistory(String filePath) {
        LinkedList<Visitor> importedHistory = new LinkedList<>();

        // Validate inputs
        if (filePath == null || filePath.isBlank()) {
            System.err.println("[ERROR] Import failed: File path is invalid/blank");
            return importedHistory;
        }

        File csvFile = new File(filePath);
        if (!csvFile.exists()) {
            System.err.println("[ERROR] Import failed: File not found (" + filePath + ")");
            return importedHistory;
        }
        if (!csvFile.isFile()) {
            System.err.println("[ERROR] Import failed: Path is not a file (" + filePath + ")");
            return importedHistory;
        }

        // Read and parse CSV
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            int lineNumber = 0;
            int successCount = 0;
            int skipCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header row (first line)
                if (lineNumber == 1) {
                    continue;
                }

                // Skip empty lines
                if (line.isBlank()) {
                    skipCount++;
                    continue;
                }

                // Parse line (handle errors per line)
                try {
                    Visitor visitor = Visitor.fromCsv(line);
                    importedHistory.add(visitor);
                    successCount++;
                } catch (IllegalArgumentException e) {
                    System.err.println("[WARNING] Skipped line " + lineNumber + ": " + e.getMessage());
                    skipCount++;
                }
            }

            // Summary for transparency
            System.out.println("[SUCCESS] Imported " + successCount + " visitors (skipped " + skipCount + " lines)");
        } catch (IOException e) {
            System.err.println("[ERROR] Import failed: " + e.getMessage());
        }

        return importedHistory;
    }
}