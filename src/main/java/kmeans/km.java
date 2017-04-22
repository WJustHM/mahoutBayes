package kmeans;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.conversion.InputDriver;
import org.apache.mahout.clustering.syntheticcontrol.canopy.Job;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.ClassUtils;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by linux on 17-2-20.
 */
public class km extends AbstractJob {

//    //     1. 第8行： InputDriver.runJob ( ) ，它用于将原始数据文件转换成 Mahout进行计算所需格式的文件 SequenceFile，它是Hadoop API提供的一种二进制文件支持。这种二进制文件直接将<key, value>对序列化到文件中。
////            2. 第11行：CanopyDriver.run( ) ，即用Canopy算法确定初始簇的个数和簇的中心。
////            3.  第14行：KMeansDriver.run( ) ，这显然是K-means算法进行聚类。
////            4. 第18~20行，ClusterDumper类将聚类的结果装换并写出来，若你了解了源代码，你也可以自己实现这个类的功能，因为聚类后的数据存储格式，往往跟自身业务有关。
//    private static String inpath = "hdfs://hadoop:9000/canopytestdata/";
//    private static String opath = "hdfs://hadoop:9000/output";
//    private static final Logger log = LoggerFactory.getLogger(km.class);
//    private static Configuration conf = new Configuration();
//    String measureClass = this.getOption("distanceMeasure");
//    DistanceMeasure measure = (DistanceMeasure) ClassUtils.instantiateAs(measureClass, DistanceMeasure.class);
//
//    public static void run(Configuration conf, Path input, Path output, DistanceMeasure measure
//            , double t1, double t2, double convergenceDelta, int maxIterations) throws InterruptedException, IOException, ClassNotFoundException {
//
//
//        log.info("Preparing Input---------------------");
//        Path directoryContainingConvertedInput = new Path(output + "_vw", "data");
//        InputDriver.runJob(input, directoryContainingConvertedInput,
//                "org.apache.mahout.math.RandomAccessSparseVector");
//
//        log.info("Running Canopy to get initial clusters");
//        //CanopyDriver.run(conf,directoryContainingConvertedInput,output,measure,t1,t2,false,false,false);
//
//
//    }
//
//
    public int run(String[] strings) throws Exception {
        return 0;
    }
}
