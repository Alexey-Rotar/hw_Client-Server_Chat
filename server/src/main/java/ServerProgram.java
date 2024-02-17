import java.io.IOException;
import java.net.ServerSocket;

public class ServerProgram {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(51000);
        Server server = new Server(serverSocket);
        server.runServer();
    }
}
