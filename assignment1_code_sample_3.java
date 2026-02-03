import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

public class SecureApp {

    // 1️⃣ Credentials moved to environment variables
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String input = scanner.nextLine();

        // 2️⃣ Basic input validation
        return input.replaceAll("[^a-zA-Z ]", "");
    }

    // 3️⃣ No OS command execution (no Runtime.exec)
    public static void sendEmail(String to, String subject, String body) {
        // Simulated safe email handling (no shell access)
        System.out.println("Sending email...");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }

    public static String getData() {
        StringBuilder result = new StringBuilder();

        try {
            // 4️⃣ HTTPS instead of HTTP
            URL url = new URL("https://secure-api.com/get-data");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }

        return result.toString();
    }

    public static void saveToDb(String data) {
        // 5️⃣ Use PreparedStatement to prevent SQL injection
        String query = "INSERT INTO mytable (column1, column2) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, data);
            pstmt.setString(2, "Another Value");

            pstmt.executeUpdate();
            System.out.println("Data saved to database.");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String userInput = getUserInput();
        String data = getData();
        saveToDb(data);
        sendEmail("admin@example.com", "User Input", userInput);
    }
}
