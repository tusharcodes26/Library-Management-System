import api.ApiServer;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port specified. Using default: 8080");
            }
        }
        
        ApiServer server = new ApiServer();
        try {
            server.start(port);
            System.out.println("API Server is running. Press Enter to terminate.");
            // Keep running until user hits Enter
            System.in.read();
            server.stop();
        } catch (IOException e) {
            System.err.println("Error starting API Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}   