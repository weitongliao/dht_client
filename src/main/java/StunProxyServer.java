import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class StunProxyServer {

    private StunProxyServer(){

    }
    //下面是代理服务器的代理，非本服务器代码
    public static void main(String[] args) throws Exception{
        Properties p=Stupid.getPZ();
        int port=Integer.parseInt(p.getProperty("port"));
        int port_=Integer.parseInt(p.getProperty("_port"));
        DatagramSocket socket=new DatagramSocket(port);
        DatagramSocket _socket=null;

        while(true){

            DatagramPacket request=new DatagramPacket(new byte[1024],1024);
            socket.receive(request);//The following is the shared code
            InetAddress clientIp=null;
            int clientPort=0;
            int _port=0;//session port from server
            String result=new String(request.getData(),"ASCII");
            String[] s=result.split(":");

            if(s.length==2){
                clientIp=InetAddress.getByName(s[0]);
                clientPort=Integer.parseInt(s[1]);
                DatagramPacket response=new DatagramPacket(new byte[1],1,clientIp,clientPort);
                socket.send(response);
            }else{
                if(_port==0){
                    _port=request.getPort();//session port from client
                    String msg=_port+"";
                    DatagramPacket response=new DatagramPacket(msg.getBytes(),msg.getBytes().length,clientIp,clientPort);
                    socket.send(response);
                }else{
                    //换个端口
                    _socket=new DatagramSocket(port_);
                    DatagramPacket response=new DatagramPacket(new byte[1],1,clientIp,clientPort);
                    _socket.send(response);
                }
            }
        }

    }
}