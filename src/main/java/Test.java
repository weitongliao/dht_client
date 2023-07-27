import java.net.DatagramSocket;
import java.net.SocketException;

public class Test {
    public static void main(String[] args) throws SocketException {
        DatagramSocket socket=new DatagramSocket(12345);
    }
}
