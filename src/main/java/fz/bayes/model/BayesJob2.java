package fz.bayes.model;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.training.WeightsMapper;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.mapreduce.VectorSumReducer;
import org.apache.mahout.math.VectorWritable;
/**
 * 贝叶斯算法第二个job任务相当于 TrainNaiveBayesJob的第二个prepareJob
 * Mapper，Reducer还用原来的
 * @author Administrator
 *
 */
public class BayesJob2 extends AbstractJob {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new BayesJob2(),args);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		addInputOption();
	    addOutputOption();
	    addOption("labelNumber","ln", "The number of the labele ");
	    if (parseArguments(args) == null) {
		      return -1;
		}
	    Path input = getInputPath();
	    Path output = getOutputPath();
	    String labelNumber=getOption("labelNumber");
	    Configuration conf=getConf();
	    conf.set(WeightsMapper.class.getName() + ".numLabels",labelNumber);
	    HadoopUtil.delete(conf, output);
	    Job job=new Job(conf);
	    job.setJobName("job2 get weightsFeture and weightsLabel by job1's output:"+input.toString());
	    job.setJarByClass(BayesJob2.class); 
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    
	    job.setMapperClass(WeightsMapper.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(VectorWritable.class);
	    job.setCombinerClass(VectorSumReducer.class);
	    job.setReducerClass(VectorSumReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(VectorWritable.class);
	    SequenceFileInputFormat.setInputPaths(job, input);
	    SequenceFileOutputFormat.setOutputPath(job, output);
	    
	    if(job.waitForCompletion(true)){
	    	return 0;
	    }
		return -1;
	}

}
