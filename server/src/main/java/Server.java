import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // Серверный сокет
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        try {
            while (!serverSocket.isClosed()) {

                // вызов accept() позволяет слушать входящие подключения
                // если входящих подключений нет, то текущий поток приостановливается
                Socket socket = serverSocket.accept(); // при подключении, получаем информацию о клиентском сокете
                System.out.println("Подключен новый клиент!");
                // при подключении нового клиента он попадает в коллекцию клиентов
                ClientManager clientManager = new ClientManager(socket);
                // и запускается новый поток для этого клиента
                Thread thread = new Thread(clientManager);
                thread.start();
            }
        }
        catch (Exception e){
            closeSocket();
        }
    }

    /**
     * Метод закрытия серверного сокета
     */
    private void closeSocket(){
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}