import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    // клиентский сокет
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private final String name;

    public Client(Socket socket, String userName) {
        this.socket = socket;
        this.name = userName;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                bufferedWriter.write(name + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        catch (Exception e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Метод прослушивания входящих сообщений
     */
    public void listenForMessage(){
        // создание нового потока для прослушивания входящих сообщений
        // нужно чтобы не блокировался главный поток (для одновременной отправки и прослушивания входящих сообщений)
        new Thread(new Runnable() {
            @Override
            public void run() {
                String incomingMessage;
                while (socket.isConnected()) { // пока соединение активно, т.е. сокет подключен к серверу
                    try {
                        // попытка считать из буфера
                        // поток будет приостановливаться, если от сервера ничего не приходит
                        incomingMessage = bufferedReader.readLine();
                        System.out.println(incomingMessage);
                    }
                    catch (Exception e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    /**
     * Метод для закрытия сокета, буферов чтения, буфера записи
     * @param socket сокет
     * @param bufferedReader буфер чтения
     * @param bufferedWriter буфер записи
     */
    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
