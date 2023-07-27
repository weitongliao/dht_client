import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Properties;

public class StunClient {

    private static String serverIp;
    private static int serverPort;
    private static int proxyPort;
    private static String proxyIp;
    private static int clientPort;

    private static boolean full_cone_nat=false;
    private static boolean cone_nat=true;
    private static boolean full_Port_Cone_nat=true;
    private static boolean own_ip=false;

    protected static DatagramSocket socket=null;
    protected static DatagramPacket request;
    protected static DatagramPacket response;
    protected static HashMap<String,Integer> hm;

    protected StunClient() throws Exception{
        Properties p=Stupid.getPZ();
        int port=Integer.parseInt(p.getProperty("serverPort"));
        String ip=p.getProperty("serverIp");
        serverIp=ip;
        serverPort=port;
        int _port=Integer.parseInt(p.getProperty("port"));
        String _ip=p.getProperty("ip");
        proxyIp=_ip;
        proxyPort=_port;
        clientPort=Integer.parseInt(p.getProperty("clientPort"));
        System.out.println(clientPort);
        socket=new DatagramSocket(clientPort);
    }

    //首先用stun探测NAT类型
    public void stun() throws Exception{
        try{
            socket.setSoTimeout(5000);
            InetAddress host=InetAddress.getByName(serverIp);
            request=new DatagramPacket("1".getBytes(),"1".getBytes().length,host,serverPort);
            response=new DatagramPacket(new byte[1024],1024);
            String result=null;
            try{
                socket.send(request);
                socket.receive(response);
                result=new String(response.getData(),0,response.getLength(),"ASCII");
            }catch(Exception e){
                //超时抛异常
                System.out.println("服务器炸了或者你的路由器坏了");
                throw new Exception("nat error");
            }
            System.out.println("stun step 1 response from server "+result);
            String[] rss=result.split(":");
            if(rss.length==0){
                int r=Integer.parseInt(result);
                if(r==0)
                    own_ip=true;
                else if(r==11)
                    full_cone_nat=true;
                else if(r==666)
                    cone_nat=false;
                return;
            }
            String ip=rss[0];
            String local=InetAddress.getLocalHost().getHostAddress();
            int NATport=Integer.parseInt(rss[1]);
            if(!ip.equals(local)){
                //客户端在内网里面
                request=new DatagramPacket("2".getBytes(),"2".getBytes().length,host,serverPort);
                response=new DatagramPacket(new byte[1024],1024);
                try{
                    socket.send(request);
                    socket.receive(response);//这里接收的是来自代理服务器
                    full_cone_nat=true;
                    System.out.println("全锥形NAT");

                }catch(Exception e){
                    //一般肯定会到这里
                    System.out.println("受限锥形NAT");
                }
                InetAddress i=InetAddress.getByName(proxyIp);
                request=new DatagramPacket(new byte[1],1,i,proxyPort);
                if(!full_cone_nat){
                    response=new DatagramPacket(new byte[1024],1024);
                    socket.send(request);
                    socket.receive(response);
                    int _port=Integer.parseInt(new String(response.getData(),"ASCII"));
                    if(_port!=NATport){
                        System.out.println("对称NAT");
                        cone_nat=false;//对称NAT 不是圆锥NAT
                        return;
                    }
                }

                socket.send(request);//在发送 让代理换个端口发回来
                try{
                    response=new DatagramPacket(new byte[1024],1024);
                    socket.receive(response);
                }catch(Exception e){
                    full_Port_Cone_nat=false;
                    System.out.println("端口受限锥形NAT");
                }

            }else{
                System.out.println("客户端拥有自己的外网ip，并不具有NAT");
                own_ip=true;
            }

        }catch(Exception e){
            throw e;
        }finally{//记录下来，下次不再探测
            DatagramPacket dp;
            String m="";
            if(!own_ip&&cone_nat){
                if(full_cone_nat){
                    m="<<<"+11;
                }else{
                    m="<<<"+10;
                }
            }else if(own_ip){
                m="<<<"+0;
            }else if(!cone_nat){
                m="<<<"+666;
            }
            if(m!=""){
                dp=new DatagramPacket(m.getBytes(),m.getBytes().length,
                        InetAddress.getByName(serverIp),serverPort);
                socket.send(dp);
            }
            System.out.println("探测NAT完成，与服务器成功建立连接");

            //if(socket!=null)
            //socket.close();
        }
    }
    public void bin(String ip,int port) throws Exception{
        InetAddress in=InetAddress.getByName(serverIp);
        String bin=">>>"+ip+":"+port;
        request=new DatagramPacket(bin.getBytes(),bin.getBytes().length,in,serverPort);
        socket.send(request);
        response=new DatagramPacket(new byte[1024],1024);
        try{
            socket.receive(response);
        }catch(Exception e){//来自目的主机的访问，肯定到不了
            System.out.println("连接成功");
        }
    }
    //获取其他客户端以及他们的NAT属性
    public void getClients() throws Exception{
        request=new DatagramPacket("3".getBytes(),"3".getBytes().length,
                InetAddress.getByName(serverIp),serverPort);

    }

    public void connect(String ip,int port) throws Exception{
        //首先探测NAT 看看能不能p2p
        synchronized(socket){
            stun();
            getClients();
            socket.notify();//唤醒其他进程，本线程进入等待状态
            socket.wait();
            String key=ip+":"+port;
            int bz=hm.get(key);
            //获得客户端B的NAT状态
            if(!cone_nat || bz==666){
                System.out.println("连接失败");
                return;
            }else if(bz==11 && !full_cone_nat){//目标客户端是全圆锥的，直联
                String bin="---";
                request=new DatagramPacket(bin.getBytes(),bin.getBytes().length,InetAddress.getByName(ip),port);
                socket.send(request);
                response=new DatagramPacket(new byte[1024],1024);
                try{
                    socket.receive(response);
                }catch(Exception e){//来自目的主机的访问，肯定到不了
                    System.out.println("连接成功");
                }
            }else if(bz==11 && full_cone_nat){//两个都是全圆锥，这种情况就不需要进行连接了
                System.out.println("连接成功");
            }else{
                bin(ip,port);
            }
            //连接结束，之后要把锁释放掉，交给p2pclientserver,让其他主机来访问本主机
            socket.notify();
        }
    }
    public static void main(String[] args){
        try{
            StunClient p =new StunClient();
            Thread t=new Thread(new StunClientServer());
            t.start();
            p.connect("0.0.0.0", -1);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}