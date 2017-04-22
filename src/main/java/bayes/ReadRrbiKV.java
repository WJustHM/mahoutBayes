package bayes;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by linux on 17-3-21.
 */
public class ReadRrbiKV {
    public static Configuration conf = new Configuration();

    static String fPath = "";
    static String trainPath = "";

    static {
        fPath = "hdfs://hadoop:9000/tmp/mahout-work-root/20news-testing/part-m-00000";
    }

    public static void main(String[] args) throws IOException {
        Map<Writable,Writable> map=readFromFile(fPath);

        Iterator<Writable> s = readFromFile(fPath).keySet().iterator();

        while (s.hasNext()) {
            Writable w = s.next();
            System.out.println(map.get(w));
        }

    }

    public static Map<Writable, Writable> readFromFile(String fpath) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(fpath), conf);
        Path path = new Path(fpath);
        Map<Writable, Writable> map = new HashMap<Writable, Writable>();
        SequenceFile.Reader readr = null;

        readr = new SequenceFile.Reader(fs, path, conf);
        Writable key = (Writable) ReflectionUtils.newInstance(readr.getKeyClass(), conf);
        Writable value = (Writable) ReflectionUtils.newInstance(readr.getValueClass(), conf);
        Class<Writable> writableClassK = (Class<Writable>) readr.getKeyClass();
        Class<Writable> writableClassV = (Class<Writable>) readr.getValueClass();
        while (readr.next(key, value)) {
            Writable k = deepCopy(key, writableClassK);
            Writable v = deepCopy(value, writableClassV);
            map.put(k, v);
        }
        IOUtils.closeStream(readr);
        return map;
    }

    public static Writable deepCopy(Writable source, Class<Writable> writableClass) {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        DataOutputStream dataout = new DataOutputStream(byteOutStream);
        Writable copieedValue = null;

        try {
            source.write(dataout);
            dataout.flush();
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            DataInput dataInput = new DataInputStream(byteInStream);
            copieedValue = writableClass.newInstance();
            copieedValue.readFields(dataInput);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return copieedValue;
    }
}
