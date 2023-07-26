// Node.java
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Node {
    private String name;
    private Socket socket;
    private NodeConnection nodeConnection;

    public Node(String name, String serverAddress, int serverPort) {
        this.name = name;
        try {
            socket = new Socket(serverAddress, serverPort);
            nodeConnection = new NodeConnection(socket);
            nodeConnection.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        nodeConnection.sendMessage(message);
    }

    public void receiveMessage(String message) {
        System.out.println(name + " received message: " + message);
    }

    public void closeNode() {
        nodeConnection.closeConnection();
    }

    public static void main(String[] args) {
        String serverAddress = "35.215.165.210";
        int serverPort = 12345;

        Node node1 = new Node("Node 1", serverAddress, serverPort);
        Node node2 = new Node("Node 2", serverAddress, serverPort);

        // Simulate sending messages between nodes
        node1.sendMessage("Hello from Node 1");
        node2.sendMessage("Hello from Node 2");

        // Close the server to exit the program
        node1.closeNode();
        node2.closeNode();
    }
}
