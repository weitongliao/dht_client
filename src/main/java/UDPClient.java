import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        final int PORT = 12345;
        try {
            // 客户端1或2自行设置isClient1的值
            boolean isClient1 = true;

            DatagramSocket clientSocket = new DatagramSocket();

            // 发送信息给服务器
            String message = "Hello, Server! I am Client " + (isClient1 ? "1" : "2");
            byte[] sendData = message.getBytes();
            InetAddress serverAddress = InetAddress.getByName("35.215.165.210");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, PORT);
            clientSocket.send(sendPacket);

            // 接收服务器回复的IP信息
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String ipInfo = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println("Received IP Info from Server: " + ipInfo);

            // 解析IP信息并获取对方的IP和端口
            String[] parts = ipInfo.split(", ");
            String clientIP = parts[0].substring(parts[0].indexOf(":") + 2);
            int clientPort = Integer.parseInt(parts[1].substring(parts[1].indexOf(":") + 2));

            // 关闭客户端Socket


            // 使用获取到的对方IP和端口进行P2P通信（这里省略了P2P通信的代码）
            System.out.println("P2P communication with Client " + (isClient1 ? "2" : "1") + ":");
            // Your P2P communication code here...
            DatagramPacket sendPacket2 = new DatagramPacket("aaaa".getBytes(), sendData.length, InetAddress.getByName(clientIP), clientPort);
            clientSocket.send(sendPacket);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
