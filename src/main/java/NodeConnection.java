// NodeConnection.java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeConnection extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private boolean connectionClosed;

    public NodeConnection(Socket socket) {
        this.socket = socket;
        connectionClosed = false;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while (!connectionClosed && (message = reader.readLine()) != null) {
                receiveMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

    public void closeConnection() {
        connectionClosed = true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
