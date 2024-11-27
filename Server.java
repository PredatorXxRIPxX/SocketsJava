import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("Server is running on port 3000");
            Socket soc = serverSocket.accept();
            System.out.println("Client connected");
            BufferedReader input = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String message = input.readLine();
            System.out.println("Client: " + message);
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            out.println("Messages received: " + message);
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }
}
