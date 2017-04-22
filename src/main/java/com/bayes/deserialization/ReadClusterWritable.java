package com.bayes.deserialization;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;
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
        String tmpseq = "hdfs://hadoop:9000/mahoutbayes/code/testmodel/-testing/part-m-00000";
        read(tmpseq);
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

    public static void read(String pathStr) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(pathStr), conf);
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(pathStr), conf);
        Text key = new Text();
        Text value = new Text();
        int i = 0;
        while (reader.next(key, value)) {
            i++;
            System.out.println("----------------key-----------------" + key);
            System.out.println("----------------value-----------------" + value);
            if (i == 100) break;
        }

    }
}
