package bayes;

import fz.bayes.BayesClassifiedJob;
import fz.bayes.BayesUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.AbstractNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.StandardNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.training.WeightsMapper;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.Map;

/**
 * Created by linux on 17-4-6.
 */
public class BayesClassifiedJobdjj extends AbstractJob {

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new BayesClassifiedJobdjj(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();
        addOption("model", "m", "The file where bayesian model store ");
        addOption("labelIndex", "labelIndex", "The file where the index store ");

        if (parseArguments(args) == null) {
            return -1;
        }
        Path input = getInputPath();
        Path output = getOutputPath();
        String modelPath = getOption("model");
        String labelIndex = getOption("labelIndex");
        System.out.println("-----------++++labelIndex:"+labelIndex);
        Configuration conf = getConf();
        conf.set("labelIndex", labelIndex);
        HadoopUtil.cacheFiles(new Path(modelPath), conf);
        HadoopUtil.delete(conf, output);
        Job job = Job.getInstance(conf);
        job.setJobName("Use bayesian model to classify the  input:" + input.getName());
        job.setJar("/home/linux/桌面/project/mahout/out/artifacts/mahout_jar/mahout.jar");
        job.setJarByClass(BayesClassifiedJobdjj.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        job.setMapperClass(BayesClassifyMapperdjj.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(VectorWritable.class);
        job.setNumReduceTasks(0);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        SequenceFileInputFormat.setInputPaths(job, input);
        SequenceFileOutputFormat.setOutputPath(job, output);

        if (job.waitForCompletion(true)) {
            return 0;
        }
        return -1;
    }
}
