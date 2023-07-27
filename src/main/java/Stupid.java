import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

//import org.Yeah.FHttp;
//import org.Yeah.FHttpSocket;

public class Stupid {

    public static Properties getPZ() throws Exception{
        Properties p=new Properties();
        BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(new File("ipAndPort.properties"))));
        p.load(in);
        return p;

    }

}