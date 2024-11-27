import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 3000);
            System.out.println("Connected to server on port 3000");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String message = input.readLine();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Server: " + in.readLine());
            socket.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }
    
}
