package bayes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * Created by linux on 17-3-27.
 */
public class UploadFile {
    static Configuration config = new Configuration();

    public static void main(String[] args) throws IOException {
//        deletetest();
//        testdata();
//
//        deletetrain();
//        traindata();

        testdelete();
        testup();
    }

    public static void testdata() throws IOException {
        String input = "/home/linux/桌面/毕业设计/毕业设计test";
        String output = "/mahoutbayes/data/";
        FileSystem hdfs = FileSystem.get(config);
        Path src = new Path(input);
        Path dst = new Path(output);
        hdfs.copyFromLocalFile(src, dst);
        hdfs.close();
    }

    public static void traindata() throws IOException {
        String input = "/home/linux/桌面/毕业设计/毕业设计train";
        String output = "/mahoutbayes/data/";
        FileSystem hdfs = FileSystem.get(config);
        Path src = new Path(input);
        Path dst = new Path(output);
        hdfs.copyFromLocalFile(src, dst);
        hdfs.close();
    }

    public static void deletetest() throws IOException {
        String uri = "hdfs://hadoop:9000";
        FileSystem fs = FileSystem.get(URI.create(uri), config);
        Path pathtest = new Path("/mahoutbayes/data/毕业设计test");
        fs.delete(pathtest, true);
    }

    public static void deletetrain() throws IOException {
        String uri = "hdfs://hadoop:9000";
        FileSystem fs = FileSystem.get(URI.create(uri), config);
        Path pathtest = new Path("/mahoutbayes/data/毕业设计train");
        fs.delete(pathtest, true);
    }

    public static void testup() throws IOException {
        String input = "/home/linux/桌面/毕业设计/测试/";
        String output = "/mahoutbayes/data/";
        FileSystem hdfs = FileSystem.get(config);
        Path src = new Path(input);
        Path dst = new Path(output);
        hdfs.copyFromLocalFile(src, dst);
        hdfs.close();
    }

    public static void testdelete() throws IOException {
        String uri = "hdfs://hadoop:9000";
        FileSystem fs = FileSystem.get(URI.create(uri), config);
        Path pathtest = new Path("/mahoutbayes/data/test");
        fs.delete(pathtest, true);
    }


}
