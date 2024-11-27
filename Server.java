import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    static ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server is listening on port 3000");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    static class ClientHandler extends Thread {
        private Socket client;
        private String name;

        public ClientHandler(Socket socket) {
            this.client = socket;
        }
        
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                
                out.println("Welcome to the server, Please enter your name:");
                String name;
                do {
                    name = in.readLine();
                    if (name == null || name.isEmpty()) {
                        out.println("Name cannot be empty. Please enter a valid name:");
                        out.println("Enter your commands:");
                    } else if (clients.containsKey(name)) {
                        out.println("This name is already taken, please enter another name:");
                        out.println("Enter your commands:");
                    } else {
                        this.name = name;
                        clients.put(name, client);
                        System.out.println(name + " has joined the server");
                        out.println("Hello " + name + ", welcome to the server!");
                    }
                } while (name == null || name.isEmpty() || clients.containsKey(name));
                out.println("Welcome " + name + "!");
                out.println("To send a message to all clients, type: @all <message>");
                out.println("To send a message to a specific client, type: @<client-name> <message>");
                out.println("To quit, type: @quit");
                out.println("Enter your commands:");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("@quit")) {
                        break;
                    } else if (message.startsWith("@all")) {
                        String[] messageParts = message.split(" ", 2);
                        String messageToSend = messageParts[1];
                        for (String clientName : clients.keySet()) {
                            if (!clientName.equals(name)) {
                                PrintWriter clientOut = new PrintWriter(clients.get(clientName).getOutputStream(), true);
                                clientOut.println(name + ": " + messageToSend);
                            }
                        }
                    } else if (message.startsWith("@")) {
                        String[] messageParts = message.split(" ", 2);
                        String clientName = messageParts[0].substring(1);
                        String messageToSend = messageParts[1];
                        if (clients.containsKey(clientName)) {
                            PrintWriter clientOut = new PrintWriter(clients.get(clientName).getOutputStream(), true);
                            clientOut.println(name + ": " + messageToSend);
                        } else {
                            out.println("Client " + clientName + " not found");
                        }
                    } else {
                        out.println("Invalid command");
                    }
                }
                
                clients.remove(name);
                client.close();
                
            } catch (Exception e) {
                System.out.println("Error handling client: " + e.getMessage());
            }
        }
    }
}