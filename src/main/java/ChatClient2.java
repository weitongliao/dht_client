import java.io.*;
import java.net.*;

public class ChatClient2 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("35.215.165.210", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Thread receiveThread = new Thread(() -> {
                try {
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message;
                    while ((message = fromServer.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            String message;
            while ((message = in.readLine()) != null) {
                out.println(message);
            }

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

