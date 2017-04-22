package canopy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.conversion.InputDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.utils.clustering.ClusterDumper;

import java.io.IOException;

public class Canpoy {
    String inpath = "hdfs://hadoop:9000/canopytestdata/sample/";
    String opath = "hdfs://hadoop:9000/canopy/output";
    String tmppath = "hdfs://hadoop:9000/dujunjie0417";

    Path in = new Path(inpath);
    Path out = new Path(opath);
    Path disxu = new Path(opath + "_vw", "data");

    public void get() throws Exception {
        try {
            Configuration conf = new Configuration();
            HadoopUtil.delete(conf, disxu);
            HadoopUtil.delete(conf, out);
            //它用于将原始数据文件转换成 Mahout进行计算所需格式的文件 SequenceFile，
            // 它是Hadoop API提供的一种二进制文件支持。这种二进制文件直接将<key, value>对序列化到文件中。
            InputDriver.runJob(in, disxu, "org.apache.mahout.math.RandomAccessSparseVector");
            //以上都是为canopy做准备数据的格式化(序列化成向量文件格式)，作为canopy的输入
            String[] str = new String[]{
                    "-i", disxu.toString(),
                    "-o", opath,
                    "-ow",
                    "-t1", "8000", "-t2", "2500", "-t3", "8000", "-t4", "2500",
//                    "-cl",
                    "--tempDir", tmppath,
            };

            //即用Canopy算法确定初始簇的个数和簇的中心
            CanopyDriver cd = new CanopyDriver();
            ToolRunner.run(conf, cd, str);
            //分析最后聚类的输出结果
            //类将聚类的结果装换并写出来，若你了解了源代码，你也可以自己实现这个类的功能，
            // 因为聚类后的数据存储格式，往往跟自身业务有关（感觉就是我自己写的那个反序列化方法（就是数据转换））
            ClusterDumper clus = new ClusterDumper(new Path(opath, "clusters-*-final"), new Path(opath, "clusteredPoints"));
            clus.printClusters(null);


//            double conver = Double.parseDouble("0.5");
//            int max = 10;
//
//            Path outpk = new Path("hdfs://hadoop:9000/km/output/dujunjie0417");
//            //这显然是K-means算法进行聚类
////            KMeansDriver.run(conf, disxu, new Path(opath, Cluster.INITIAL_CLUSTERS_DIR + "-final"), outpk,
////                    conver, max, true, 0.0, false);
//            HadoopUtil.delete(conf, outpk);
//            String tmppk = "hdfs://hadoop:9000/tmp/CK/dujunjie0417";
//            String[] arg = new String[]{
//                    "-i", disxu.toString(),
//                    "-c", opath + "/" + Cluster.INITIAL_CLUSTERS_DIR + "-final",
//                    "-o", outpk.toString(),
//                    "-ow",
//                    "-x", "10",
//                    "-cl",
//                    "--tempDir", tmppk,
//            };
//
////            String[] arg = new String[]{
////                    "-i", disxu.toString(),
////                    "-c", opath + "/" + Cluster.INITIAL_CLUSTERS_DIR + "-final",
////                    "-k", "2",
////                    "-o", outpk.toString(),
////                    "-x", "5",
////                    "-cl",
////
////            };
//
//            KMeansDriver kd = new KMeansDriver();
//            ToolRunner.run(conf, kd, arg);
//
//            HadoopUtil.delete(conf, new Path(tmppath));
//            HadoopUtil.delete(conf, new Path(tmppk));
//
//            ClusterDumper clusk = new ClusterDumper(new Path(outpk, "clusters-*-final"), new Path(outpk, "clusteredPoints"));
//            clusk.printClusters(null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws Exception {
        Canpoy ca = new Canpoy();
        ca.get();
    }
}
