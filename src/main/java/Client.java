// Client.java
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            // Connect to the server
            String serverAddress = "35.215.165.210";
            int serverPort = 12345;
            Socket socket = new Socket(serverAddress, serverPort);

            // Receive client info from the server
            byte[] buffer = new byte[1024];
            int bytesRead = socket.getInputStream().read(buffer);
            String clientInfo = new String(buffer, 0, bytesRead);
            String[] parts = clientInfo.split(",");
            String otherClientIP = parts[0];
            int otherClientPort = Integer.parseInt(parts[1]);

            // Close the server connection
            socket.close();

            // Connect to the other client directly
            Socket directSocket = new Socket(otherClientIP, otherClientPort);

            // Now both clients (this and the other) are connected directly, and they can exchange messages.
            // Here, you can implement your P2P communication logic between the two clients.
            // You can use directSocket.getInputStream() and directSocket.getOutputStream() to send/receive data.
            OutputStream outputStream = directSocket.getOutputStream();
            InputStream inputStream = directSocket.getInputStream();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a message to send to the other client: ");
            String messageToSend = scanner.nextLine();
            outputStream.write(messageToSend.getBytes());


            byte[] receivedBuffer = new byte[1024];
            int bytesReceived = inputStream.read(receivedBuffer);
            String receivedMessage = new String(receivedBuffer, 0, bytesReceived);
            System.out.println("Received message from the other client: " + receivedMessage);

            // Remember to handle exceptions and close sockets properly in a real-world implementation.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//import java.io.*;
//import java.net.*;
//
//public class Client {
//    public static void main(String[] args) {
//        try {
//            // 指定Server的IP地址和监听端口号
//            String serverIP = "35.215.165.210"; // 替换为Server的实际IP地址
//            int port = 12345;
//
//            // 创建Socket连接到Server
//            Socket socket = new Socket(serverIP, port);
//
//            // 获取Client的IP地址和端口号
//            String clientIP = socket.getLocalAddress().getHostAddress();
//            int clientPort = socket.getLocalPort();
//            System.out.println("Connected to Server. Local IP: " + clientIP + ", Local Port: " + clientPort);
//
//            // 发送消息给Server
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            String message = "Hello, Server!";
//            out.println(message);
//
//            // 关闭连接
//            out.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
