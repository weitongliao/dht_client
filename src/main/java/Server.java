// Server.java
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<NodeConnection> nodeConnections;

    public Server() {
        nodeConnections = new ArrayList<>();
    }

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                NodeConnection nodeConnection = new NodeConnection(socket);
                nodeConnections.add(nodeConnection);
                nodeConnection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeServer() {
        for (NodeConnection nodeConnection : nodeConnections) {
            nodeConnection.closeConnection();
        }
    }

    public static void main(String[] args) {
        int port = 12345;
        Server server = new Server();
        server.start(port);
    }
}



//import java.io.*;
//import java.net.*;
//
//public class Server {
//    public static void main(String[] args) {
//        while(true){
//            try {
//                // 创建ServerSocket，并指定监听端口号
//                int port = 12345;
//                ServerSocket serverSocket = new ServerSocket(port);
//                System.out.println("Server is listening on port " + port);
//
//                // 等待客户端连接
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("Client connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
//
//                // 读取客户端发送的消息
//                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                String message = in.readLine();
//                System.out.println("Client message: " + message);
//
//                // 关闭连接
//                in.close();
//                clientSocket.close();
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
