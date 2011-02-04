import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;


public class FTP extends FTPClient {
    public FTP(String server) throws SocketException, IOException {
        connect(server);
        login("anonymous", "anonymous");
    }
}
