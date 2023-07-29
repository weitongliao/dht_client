import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendData {
    public static void main(String[] args) {
        try {
            String ipAddress = "31.94.29.193"; // 目标IP地址
            int port = 57104; // 目标端口号

            // 创建发送用的DatagramSocket对象
            DatagramSocket socket = new DatagramSocket();

            // 发送的消息内容
            String message = "Hello, UDP Receiver!";
            byte[] sendData = message.getBytes();

            // 创建用于发送的DatagramPacket对象
            InetAddress address = InetAddress.getByName(ipAddress);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);

            // 发送数据包
            socket.send(sendPacket);

            // 关闭Socket
            socket.close();

            System.out.println("UDP packet sent to " + ipAddress + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
