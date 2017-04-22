package com.bayes.preparedata;

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
        deletetest();
        testdata();

        deletetrain();
        traindata();
    }

    public static void testdata() throws IOException {
        String input = System.getProperty("user.dir") + "/Testdataclean";
        String output = "/mahoutbayes/data/";
        FileSystem hdfs = FileSystem.get(config);
        Path src = new Path(input);
        Path dst = new Path(output);
        hdfs.copyFromLocalFile(src, dst);
        hdfs.close();
    }

    public static void traindata() throws IOException {
        String input = System.getProperty("user.dir") + "/Trainingdataclean";
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
        Path pathtest = new Path("/mahoutbayes/data/testdata");
        fs.delete(pathtest, true);
    }

    public static void deletetrain() throws IOException {
        String uri = "hdfs://hadoop:9000";
        FileSystem fs = FileSystem.get(URI.create(uri), config);
        Path pathtest = new Path("/mahoutbayes/data/traindata");
        fs.delete(pathtest, true);
    }
}
