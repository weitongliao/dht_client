import java.net.DatagramPacket;
import java.net.InetAddress;

public class StunClientServer extends StunClient implements Runnable{

    protected StunClientServer() throws Exception {
        super();
    }
    private StringBuffer sb=null;
    @Override
    public void run() {
        synchronized(socket){
            try{
                socket.wait();//上来就堵住
                DatagramPacket request=new DatagramPacket(new byte[500],500);
                while(true){
                    socket.receive(request);

                    String bin=new String(request.getData(),"ASCII");
                    if(bin.startsWith(">>>")){//请求客户端向某客户端打洞，来建立点对点连接
                        bin=bin.substring(3, bin.length());
                        String[] bs=bin.split(":");
                        bin=">>>"+request.getAddress().getHostAddress()+":"+request.getPort();
                        DatagramPacket response=new DatagramPacket("---".getBytes(),
                                "---".getBytes().length,InetAddress.getByName(bs[0]),Integer.parseInt(bs[1]));
                        socket.send(response);
                    }else{
                        //接收server 3的发送过来的所有客户端的相关数据，NAT类型，ip,port等
                        if(!"-1".equals(bin)){
                            sb.append(bin.trim());
                        }
                        String[] sm=sb.toString().split(" ");
                        for(int i=0;i<sm.length;i++){
                            String[] op=sm[i].split(":");
                            hm.put(op[0]+":"+op[1], Integer.parseInt(op[2]));
                            //这里hm是变化中的所以就不做null的判断了
                        }
                        socket.notify();//唤醒父类的线程
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}