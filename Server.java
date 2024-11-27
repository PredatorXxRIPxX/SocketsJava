import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {
    public static String getFactorial(int n) {
        long fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return "Factorial of " + n + " is " + fact;
    }
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server is running on port 3000");
            
            while (true) {
                try (Socket soc = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                     PrintWriter out = new PrintWriter(soc.getOutputStream(), true)) {
                    
                    System.out.println("Client connected");
                    
                    String message = input.readLine();
                    System.out.println("Client: " + message);
                    
                    out.println("Message received: " + message);
                    
                    try {
                        int number = Integer.parseInt(message);
                        String response = getFactorial(number);
                        out.println(response);
                    } catch (NumberFormatException e) {
                        out.println("Invalid input. Please enter a valid integer.");
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}