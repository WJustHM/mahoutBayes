package canopy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFilter;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.math.VectorWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by linux on 17-2-17.
 */
public class ReadClusterWritable extends AbstractJob {
    private static Job job;
    private static Configuration conf = new Configuration();
    public static void main(String[] args) throws Exception {
        Path path = new Path(args[1]);
        FileSystem fs = path.getFileSystem(conf);
        if (fs.exists(path)) {
            fs.delete(path, true);
        }
        ToolRunner.run(conf,new ReadClusterWritable(),args);
    }

    public int run(String[] strings) throws Exception {
        job = Job.getInstance();
//        job.setJar("/home/linux/IdeaProjects/mahout/out/artifacts/mahout_jar/mahout.jar");
        job.setJarByClass(ReadClusterWritable.class);
        job.setMapperClass(map.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setNumReduceTasks(0);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static class map extends Mapper<Text, ClusterWritable, Text, Text> {
        private Logger log = LoggerFactory.getLogger(map.class);

        @Override
        protected void map(Text key, ClusterWritable value, Context context) throws IOException, InterruptedException {
            String str = value.getValue().getCenter().asFormatString();
            log.info("center***************"+str);
            context.write(key,new Text(str));
        }
    }
}
