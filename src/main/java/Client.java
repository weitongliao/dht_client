import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            // 指定Server的IP地址和监听端口号
            String serverIP = "35.215.165.210"; // 替换为Server的实际IP地址
            int port = 12345;

            // 创建Socket连接到Server
            Socket socket = new Socket(serverIP, port);

            // 获取Client的IP地址和端口号
            String clientIP = socket.getLocalAddress().getHostAddress();
            int clientPort = socket.getLocalPort();
            System.out.println("Connected to Server. Local IP: " + clientIP + ", Local Port: " + clientPort);

            // 发送消息给Server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String message = "Hello, Server!";
            out.println(message);

            // 关闭连接
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
