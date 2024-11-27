import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static String getFactorial(int n) {
        long fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return "Factorial of " + n + " is " + fact;
    }
    public static void main(String [] args){
        try(ServerSocket serverSocket = new ServerSocket(3000)){
            System.out.println("Server is listining on port 3000");
            while (true) {
                Socket clienSocket = serverSocket.accept();
                new ClientHandler(clienSocket).start();
            }

        }catch(Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    static class ClientHandler extends Thread {
        private Socket client;
        public ClientHandler(Socket socket) {
            this.client = socket;
        }
        @Override
        public void run(){
            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                String line;
                while ((line = in.readLine()) != null) {
                    if ("quit".equalsIgnoreCase(line)) {
                        break;
                    }
                    int number = Integer.parseInt(line);
                    String response = getFactorial(number);
                    out.println(response);
                }
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}