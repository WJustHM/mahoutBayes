package bayes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.StringTuple;
import org.apache.mahout.math.VectorWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

/**
 * Created by linux on 17-2-17.
 */
public class ReadClusterWritable extends AbstractJob {
    private static SequenceFile.Reader reader = null;
    private static Job job;
    private static Configuration conf = new Configuration();

    public static void main(String[] args) throws Exception {
//        Path path = new Path(args[1]);
//        FileSystem fs = path.getFileSystem(conf);
//        if (fs.exists(path)) {
//            fs.delete(path, true);
//        }

        String seqtest="hdfs://ubuntu:9000/mahoutbayes/code/seqdirtrain/part-m-00000";
        String seqwordcount="hdfs://ubuntu:9000/mahoutbayes/code/train-seq2parse/wordcount/part-r-00000";
        String seqtokenizeddocuments="hdfs://ubuntu:9000/mahoutbayes/code/train-seq2parse/tokenized-documents/part-m-00000";
        String seqdfcount="hdfs://ubuntu:9000/mahoutbayes/code/train-seq2parse/df-count/part-r-00000";
        String seqtfidfvectors="hdfs://ubuntu:9000/tmp/mahout-work-root/20news-vectors/tfidf-vectors/part-r-00000";
        String tesmodel="hdfs://ubuntu:9000/mahoutbayes/code/testmodel/-testing/part-m-00000";
        String test="hdfs://hadoop:9000/bayes/test/-testing/part-m-00000";
        String index="hdfs://hadoop:9000/bayes/train/model/naiveBayesModel.bin";
        String dfcount="hdfs://hadoop:9000/bayes/seq2parse/df-count/part-r-00000";
        String tdf="hdfs://hadoop:9000/bayes/seq2parse/tf-vectors/part-r-00000";
        String tstout="hdfs://hadoop:9000/tmp/mahout-work-root/20news-testing/part-m-00000";
        String tests="hdfs://hadoop:9000/mahoutbayes/code/testmodel/-testing/part-m-00000";
        String selfdata="hdfs://hadoop:9000/mahoutbayes/code/train-seq2parse/tfidf-vectors/part-r-00000";
        String splitdata="hdfs://hadoop:9000/mahoutbayes/split/-train-vectors/part-r-00000";

        String  tmpseq="hdfs://ubuntu:9000/tmp/mahout-work-root/20news-seq/part-m-00000";
        read(tesmodel);
    }

    public int run(String[] strings) throws Exception {
        job = Job.getInstance();
        job.setJarByClass(ReadClusterWritable.class);
        job.setMapperClass(map.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        Path path = new Path(strings[0]);
        FileSystem fs = FileSystem.get(conf);
        reader = new SequenceFile.Reader(fs, path, conf);
        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static class map extends Mapper<LongWritable, Text, Text, Text> {
        private Logger log = LoggerFactory.getLogger(map.class);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            key = (LongWritable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            value = (Text) ReflectionUtils.newInstance(reader.getValueClass(), conf);

            System.out.printf("%s\t%s\n", key, value);
            log.info("%s\t%s\n", key, value);
            context.write(new Text("----------------key-----------------" + key), new Text("--------value--------" + value));

        }
    }
    public static void read(String pathStr) throws IOException{
        FileSystem fs=FileSystem.get(URI.create(pathStr), conf);
        SequenceFile.Reader reader=new SequenceFile.Reader(fs, new Path(pathStr), conf);
        Text key=new Text();
        Text value=new Text();
        int i=0;
        while(reader.next(key, value)){
            i++;
            System.out.println("----------------key-----------------"+key);
            System.out.println("----------------value-----------------"+value);
        }

    }
}
