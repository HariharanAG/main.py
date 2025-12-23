import java.util.*;
import java.io.*;


public class SmartDuplicateDetectionEngine {

    private static HashSet<String> uniqueSet = new HashSet<>();
    private static HashMap<String, Integer> frequencyMap = new HashMap<>();
    private static ArrayList<String> inputHistory = new ArrayList<>();
    private static final String FILE_NAME = "data.txt";

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
       loadDataFromFile();
       showStartupSummary();

        while (true) {
            printMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    addSingleEntry();
                    break;
                case 2:
                    addBulkEntries();
                    break;
                case 3:
                    viewUniqueEntries();
                    break;
                case 4:
                    viewDuplicateEntries();
                    break;
                case 5:
                    viewFrequencyReport();
                    break;
                case 6:
                    clearAllData();
                    break;
                case 7:
                    System.out.println("Exiting application. Goodbye.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ---------------- MENU ----------------

    private static void printMenu() {
        System.out.println("\n=== Smart Duplicate Detection Engine ===");
        System.out.println("1. Add Data Entry");
        System.out.println("2. Add Bulk Data");
        System.out.println("3. View Unique Entries (Sorted)");
        System.out.println("4. View Duplicate Entries");
        System.out.println("5. View Frequency Report");
        System.out.println("6. Clear All Data");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        int choice = sc.nextInt();
        sc.nextLine();
        return choice;
    }

    // ---------------- OPERATIONS ----------------

    private static void addSingleEntry() {
        System.out.print("Enter value: ");
        String value = normalize(sc.nextLine());

        if (value.isEmpty()) {
            System.out.println("Empty input ignored.");
            return;
        }

        boolean isDuplicate = frequencyMap.containsKey(value);
        processEntry(value);

        if (isDuplicate) {
            System.out.println("Duplicate entry detected: " + value);
        } else {
            System.out.println("New entry added: " + value);
        }

        showMostFrequent();
    }

    private static void addBulkEntries() {
        System.out.print("Enter comma-separated values: ");
        String input = sc.nextLine();

        String[] values = input.split(",");
        int count = 0;

        for (String val : values) {
            String normalized = normalize(val);
            if (!normalized.isEmpty()) {
                processEntry(normalized);
                count++;
            }
        }

        System.out.println(count + " entries processed.");
        showMostFrequent();
    }
    private static void viewUniqueEntries() {
        if (uniqueSet.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        List<String> sortedList = new ArrayList<>(uniqueSet);
        Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);


        System.out.println("\nUnique Entries (Sorted):");
        for (String value : sortedList) {
            System.out.println(value);
        }
    }

    private static void viewDuplicateEntries() {
        boolean found = false;

        System.out.println("\nDuplicate Entries:");
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No duplicates found.");
        }
    }

    private static void viewFrequencyReport() {
        if (frequencyMap.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        System.out.println("\nFrequency Report:");
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    private static void clearAllData() {
        uniqueSet.clear();
        frequencyMap.clear();
        inputHistory.clear();
        System.out.println("All data cleared.");
    }

    // ---------------- EXTRA FEATURE ----------------

    private static void showMostFrequent() {
        if (frequencyMap.isEmpty()) return;

        String maxKey = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxKey = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        System.out.println("Most frequent so far: " + maxKey + " (" + maxCount + " times)");
    }

    // ---------------- UTILITY ----------------

    private static String normalize(String input) {
        return input.trim().toLowerCase();
    }

    private static void loadDataFromFile() {
    File file = new File(FILE_NAME);
    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String value = normalize(line);
            if (!value.isEmpty()) {
                processEntry(value);
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading data file.");
    }
}
private static void showStartupSummary() {
    System.out.println("\n--- Data Summary ---");
    System.out.println("Total entries: " + inputHistory.size());
    System.out.println("Unique entries: " + uniqueSet.size());

    long duplicates = frequencyMap.values().stream()
            .filter(count -> count > 1)
            .count();

    System.out.println("Duplicate values: " + duplicates);
    System.out.println("--------------------\n");
}
private static void saveToFile(String value) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
        bw.write(value);
        bw.newLine();
    } catch (IOException e) {
        System.out.println("Error writing to data file.");
    }
}
private static void processEntry(String value) {
    inputHistory.add(value);
    uniqueSet.add(value);
    frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
    saveToFile(value);   // AUTO SAVE
}

}
