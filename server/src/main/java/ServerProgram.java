import java.io.IOException;
import java.net.ServerSocket;

public class ServerProgram {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5100);
        Server server = new Server(serverSocket);
        server.runServer();
    }
}
