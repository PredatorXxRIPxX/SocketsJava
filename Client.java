import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3000);
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server on port 3000");

            while (true) {
                System.out.println(in.readLine());
                if (in.readLine()=="Enter your commands:") {
                    String message = input.readLine();  
                    out.println(message);
                }else{
                    System.out.println(in.readLine());
                }
                 
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}