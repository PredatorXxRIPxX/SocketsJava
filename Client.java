import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 3000);
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            Scanner scanner = new Scanner(System.in);
            
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();
            
            while (true) {
                String message = scanner.nextLine();
                out.println(message);
                
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            
            scanner.close();
            socket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}