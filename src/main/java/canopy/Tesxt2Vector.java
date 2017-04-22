package canopy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;

/**
 * Created by linux on 17-2-16.
 */
public class Tesxt2Vector extends AbstractJob {
    private static Job job;
    private static Configuration conf = new Configuration();

    public static void main(String[] args) throws Exception {
        Path path = new Path(args[1]);
        FileSystem fs = path.getFileSystem(conf);
        if (fs.exists(path)) {
            fs.delete(path, true);
        }

        new Tesxt2Vector().run(args);
    }

    public int run(String[] strings) throws Exception {
        this.addInputOption();
        this.addOutputOption();
        job = Job.getInstance();
        job.setJarByClass(Tesxt2Vector.class);
        job.setMapperClass(map.class);
        job.setReducerClass(reduce.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(VectorWritable.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        //序列化的格式输出
        FileInputFormat.addInputPath(job, new Path(strings[0]));
        SequenceFileOutputFormat.setOutputPath(job, new Path(strings[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static class map extends Mapper<LongWritable, Text, LongWritable, VectorWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] str = value.toString().split("\\s{1,}");
            Vector vector = new RandomAccessSparseVector(str.length);
            for (int i = 0; i < str.length; i++) {
                vector.set(i, Double.parseDouble(str[i]));
            }
            VectorWritable va = new VectorWritable(vector);
            context.write(key, va);
        }
    }

    public static class reduce extends Reducer<LongWritable, VectorWritable, LongWritable, VectorWritable> {

        @Override
        protected void reduce(LongWritable key, Iterable<VectorWritable> values, Context context) throws IOException, InterruptedException {
            for (VectorWritable v : values) {
                context.write(key, v);
            }
        }
    }
}
