import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(3000);
            System.out.println("Server started on port 3000");
            
            while (true) {
                Socket socket = server.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static class ClientHandler extends Thread {
        private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            
                while (username != null ) {
                    out.println("Please enter your username:");
                    String potentialUsername = in.readLine();
                    
                    if (potentialUsername == null || potentialUsername.trim().isEmpty()) {
                        out.println("Username cannot be empty.");
                        continue;
                    }
                    
                    if (clients.containsKey(potentialUsername)) {
                        out.println("Username already taken. Please choose another.");
                        continue;
                    }
                    
                    username = potentialUsername;
                    clients.put(username, this);
                    out.println("Welcome, " + username + "!");
                    break;
                }
                
                ClientHandler friendHandler = null;
                while (friendHandler == null) {
                    out.println("Enter the username of the friend you want to chat with:");
                    String friendName = in.readLine();
                    
                    friendHandler = clients.get(friendName);
                    if (friendHandler == null) {
                        out.println("User not found. Please try again.");
                    }
                }
                
                out.println("Connected to " + friendHandler.username);
                friendHandler.out.println(username + " has connected to chat with you.");
                
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        out.println("You have left the chat.");
                        friendHandler.out.println(username + " has left the chat.");
                        break;
                    }
                    
                    friendHandler.out.println(username + ": " + message);
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (username != null) {
                    clients.remove(username);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}