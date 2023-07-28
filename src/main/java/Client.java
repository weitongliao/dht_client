import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.ice.Agent;
import org.ice4j.ice.IceMediaStream;
import org.ice4j.ice.harvest.StunCandidateHarvester;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            // 加入服务器
            DatagramSocket clientSocket = new DatagramSocket();

            String request = "join";
            byte[] sendData = request.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("35.215.165.210"), 12345);
            clientSocket.send(sendPacket);
            System.out.println("aa");
            // 接收服务器响应
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            System.out.println("bb");
            // 解析服务器响应，获取节点信息
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] info = response.split(",");
            int nodeId = Integer.parseInt(info[0]);
            String nodeIP = info[1];
            int nodePort = Integer.parseInt(info[2]);

            // 关闭与服务器的连接
//            clientSocket.close();
            System.out.println(response);

            Agent agent = new Agent(); // A simple ICE Agent

/*** Setup the STUN servers: ***/
            String[] hostnames = new String[] {"jitsi.org","numb.viagenie.ca","stun.ekiga.net"};
// Look online for actively working public STUN Servers. You can find free servers.
// Now add these URLS as Stun Servers with standard 3478 port for STUN servrs.
            for(String hostname: hostnames){
                try {
                    // InetAddress qualifies a url to an IP Address, if you have an error here, make sure the url is reachable and correct
                    TransportAddress ta = new TransportAddress(InetAddress.getByName(hostname), 3478, Transport.UDP);
                    // Currently Ice4J only supports UDP and will throw an Error otherwise
                    agent.addCandidateHarvester(new StunCandidateHarvester(ta));
                } catch (Exception e) { e.printStackTrace();}
            }
            IceMediaStream stream = agent.createMediaStream("audio");
            int port = 5000; // Choose any port
            agent.createComponent(stream, Transport.UDP, port, port, port+100);

            DatagramPacket packet = new DatagramPacket(new byte[10000],10000);
//            packet.setAddress(hostname);
//            packet.setPort(port);
//            wrapper.send(packet);
// The three last arguments are: preferredPort, minPort, maxPort
            // 客户端之间直接进行P2P通信
            // TODO: 在此处实现P2P通信逻辑，使用nodeIP和nodePort进行P2P连接
//            try {
                // 创建DatagramSocket对象
//                DatagramSocket new_clientSocket = new DatagramSocket();

                // 启动接收消息线程
//                new Thread(() -> {
//                    byte[] new_receiveData = new byte[1024];
//                    while (true) {
//                        try {
//                            System.out.println("receive");
//                            DatagramPacket new_receivePacket = new DatagramPacket(new_receiveData, new_receiveData.length);
//                            clientSocket.receive(new_receivePacket);
//                            String message = new String(new_receivePacket.getData(), 0, new_receivePacket.getLength());
//                            System.out.println("收到来自节点1的消息：" + message);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();

                // 发送消息给节点1
//                Scanner scanner = new Scanner(System.in);
//                while (true) {
//                    System.out.print("请输入要发送的消息：");
//                    String message = scanner.nextLine();
//                    byte[] new_sendData = message.getBytes();
//                    DatagramPacket new_sendPacket = new DatagramPacket(new_sendData, new_sendData.length, InetAddress.getByName(nodeIP), nodePort);
//                    clientSocket.send(new_sendPacket);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//// Client.java
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class Client {
//    public static void main(String[] args) {
//        try {
//            // Connect to the server
//            String serverAddress = "35.215.165.210";
//            int serverPort = 12345;
//            Socket socket = new Socket(serverAddress, serverPort);
//
//            // Receive client info from the server
//            byte[] buffer = new byte[1024];
//            int bytesRead = socket.getInputStream().read(buffer);
//            String clientInfo = new String(buffer, 0, bytesRead);
//            String[] parts = clientInfo.split(",");
//            String otherClientIP = parts[0];
//            int otherClientPort = Integer.parseInt(parts[1]);
//
//            System.out.println("get ip"+otherClientIP+otherClientPort);
//            // Close the server connection
//            socket.close();
//
//            // Connect to the other client directly
//            Socket directSocket = new Socket(otherClientIP, otherClientPort);
//            System.out.println("ok");
//            // Now both clients (this and the other) are connected directly, and they can exchange messages.
//            // Here, you can implement your P2P communication logic between the two clients.
//            // You can use directSocket.getInputStream() and directSocket.getOutputStream() to send/receive data.
//            OutputStream outputStream = directSocket.getOutputStream();
//            InputStream inputStream = directSocket.getInputStream();
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Enter a message to send to the other client: ");
//            String messageToSend = scanner.nextLine();
//            outputStream.write(messageToSend.getBytes());
//
//
//            byte[] receivedBuffer = new byte[1024];
//            int bytesReceived = inputStream.read(receivedBuffer);
//            String receivedMessage = new String(receivedBuffer, 0, bytesReceived);
//            System.out.println("Received message from the other client: " + receivedMessage);
//
//            // Remember to handle exceptions and close sockets properly in a real-world implementation.
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

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
