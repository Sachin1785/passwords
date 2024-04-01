package passwords;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class pass {
    static final int PASSWORD_LENGTH = 12;
    static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
    static final String FILE_NAME = "passwords.csv";
    static List<PasswordEntry> passwords = new ArrayList<>();

    // ANSI color codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        loadPasswordsFromFile();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(ANSI_BLUE + "1. Generate a new password" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "2. Display all passwords" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "3. Search for a password" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "4. Exit" + ANSI_RESET);
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    generatePassword(scanner);
                    break;
                case 2:
                    displayPasswords();
                    break;
                case 3:
                    searchPassword(scanner);
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
            }
        }
    }

    static void generatePassword(Scanner scanner) {
        System.out.print("Enter website name: ");
        String website = scanner.next();
        System.out.print("Enter email address: ");
        String email = scanner.next();
        String password = generateRandomPassword();
        PasswordEntry entry = new PasswordEntry(website, email, password);
        passwords.add(entry);
        savePasswordsToFile();
        System.out.println(ANSI_GREEN + "New password generated: " + password + ANSI_RESET);
    }

    static String generateRandomPassword() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    static void savePasswordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (PasswordEntry entry : passwords) {
                writer.write(entry.getWebsite() + "," + entry.getEmail() + "," + entry.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Error writing to file: " + e.getMessage() + ANSI_RESET);
        }
    }

    static void loadPasswordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String website = parts[0];
                    String email = parts[1];
                    String password = parts[2];
                    PasswordEntry entry = new PasswordEntry(website, email, password);
                    passwords.add(entry);
                }
            }
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Error reading file: " + e.getMessage() + ANSI_RESET);
        }
    }

    private static void displayPasswords() {
        if (passwords.isEmpty()) {
            System.out.println(ANSI_YELLOW + "No passwords stored yet." + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Stored passwords:" + ANSI_RESET);
            for (PasswordEntry entry : passwords) {
                System.out.println("Website: " + entry.getWebsite() + ", Email: " + entry.getEmail() + ", Password: " + entry.getPassword());
            }
        }
    }

    private static void searchPassword(Scanner scanner) {
        System.out.print("Enter website name: ");
        String website = scanner.next();
        boolean found = false;
        for (PasswordEntry entry : passwords) {
            if (entry.getWebsite().equalsIgnoreCase(website)) {
                System.out.println("Website: " + entry.getWebsite() + ", Email: " + entry.getEmail() + ", Password: " + entry.getPassword());
                found = true;
            }
        }
        if (!found) {
            System.out.println(ANSI_RED + "No password found for website: " + website + ANSI_RESET);
        }
    }

    private static class PasswordEntry {
        private String website;
        private String email;
        private String password;

        public PasswordEntry(String website, String email, String password) {
            this.website = website;
            this.email = email;
            this.password = password;
        }

        public String getWebsite() {
            return website;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
