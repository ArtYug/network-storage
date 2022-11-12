import forimage.CloudinhoNotForCloudServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server Started");
            while (true) {
                Socket socket = serverSocket.accept();
                CloudinhoNotForCloudServer handler = new CloudinhoNotForCloudServer(socket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
