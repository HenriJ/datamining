import java.util.Stack;

import org.apache.commons.net.ftp.FTPFile;

public class Debug {

    public static void main(String[] args) throws Exception {
        DB db = DB.get();

        String server = "riton";
        String path;

        Stack<String> foldersToExplore = new Stack<String>();
        foldersToExplore.push("/");

        FTP ftp =  new FTP(server);

        Query query = db.newQuery(
                "INSERT INTO  files " +
                        "SET  server = ? , path = ? , " +
                        "       name = ? , size = ? , " +
                        "       type = ? ,  timechecked = NOW()");

        while (foldersToExplore.size() > 0) {
            path = foldersToExplore.pop();
            ftp.changeWorkingDirectory(path);
            FTPFile[] files = ftp.listFiles();
            for(FTPFile file : files) {
                System.out.println(path + file.getName());
                if (file.getType() == FTPFile.FILE_TYPE) {
                    long size = file.getSize();
                    if (size > 0) {
                        String name = file.getName();

                        query.clearParameters();
                        query.setInt(1, 0);
                        query.setString(2, path);
                        query.setString(3, name);
                        query.setLong(4, size);
                        query.setInt(6, 0);

                        query.execute();
                    }
                } else {
                    foldersToExplore.push(path + file.getName() + "/");
                }
            }
        }

        System.out.println(db);
    }

}
