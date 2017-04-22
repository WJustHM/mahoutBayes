package fz.bayes.model;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.MRConfig;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.training.ThetaMapper;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SparseMatrix;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import com.google.common.base.Preconditions;

import fz.bayes.model.util.OperateArgs;

public class WriteBayesModel extends OperateArgs{

	/**
	 * @param args,输入和输出都是没有用的，输入是job1和job 2 的输出，输出是model的路径
	 * model存储的路径是 输出路径下面的naiveBayesModel.bin文件
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		String[] arg={
				"-i","",
				"-o","",
				"-mp","hdfs://node33:8020/user/root/bayes/output001/bayesModel",
				"-bj1","hdfs://node33:8020/user/root/bayes/output001/job1",
				"-bj2","hdfs://node33:8020/user/root/bayes/output001/job2"};
		new WriteBayesModel().run(arg);
	}
	/**
	 * 把model写入文件中
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public  int run(String[] args) throws IOException, ParseException{
	
		//fs
//		setOption("fs","fileSystem",true,"the file system",true);  
		// modelPath
        setOption("mp","modelPath",true,"the path for bayesian model to store",true);  
        // bayes job 1 path
        setOption("bj1","bayesJob1",true,"the path for bayes job 1",true);  
        // bayes job 2 path
        setOption("bj2","bayesJob2",true,"the path for bayes job 2",true);  
		if(!parseArgs(args)){
			return -1;
		}
		String job1Path=getNameValue("bj1");
		String job2Path=getNameValue("bj2");
		Configuration conf=getConf();
		// just for test *************
		/*conf.set("fs.defaultFS", "hdfs://node33:8020");  
	    conf.set("mapreduce.framework.name", "yarn");  
	    conf.set("yarn.resourcemanager.address", "node33:8032");*/
	    conf.set(MRConfig.MAPREDUCE_APP_SUBMISSION_CROSS_PLATFORM, "true");
	    // ************************just for test;
//		String fs= getNameValue("fs");
//		conf.set("fs.default.name", fs);
		String modelPath=getNameValue("mp");
		NaiveBayesModel naiveBayesModel=readFromPaths(job1Path,job2Path,conf);
		naiveBayesModel.validate();
	    naiveBayesModel.serialize(new Path(modelPath), conf);
	    System.out.println("Write bayesian model to '"+modelPath+"/naiveBayesModel.bin'");
	    return 0;
	}
	/**
	 * 摘自BayesUtils的readModelFromDir方法，只修改了相关路径
	 * @param job1Path
	 * @param job2Path
	 * @param conf
	 * @return
	 */
	public  NaiveBayesModel readFromPaths(String job1Path,String job2Path,Configuration conf){
		float alphaI = conf.getFloat(ThetaMapper.ALPHA_I, 1.0f);
	    // read feature sums and label sums
	    Vector scoresPerLabel = null;
	    Vector scoresPerFeature = null;
	    for (Pair<Text,VectorWritable> record : new SequenceFileDirIterable<Text, VectorWritable>(
	        new Path(job2Path), PathType.LIST, PathFilters.partFilter(), conf)) {
	      String key = record.getFirst().toString();
	      VectorWritable value = record.getSecond();
	      if (key.equals(TrainNaiveBayesJob.WEIGHTS_PER_FEATURE)) {
	        scoresPerFeature = value.get();
	      } else if (key.equals(TrainNaiveBayesJob.WEIGHTS_PER_LABEL)) {
	        scoresPerLabel = value.get();
	      }
	    }

	    Preconditions.checkNotNull(scoresPerFeature);
	    Preconditions.checkNotNull(scoresPerLabel);

	    Matrix scoresPerLabelAndFeature = new SparseMatrix(scoresPerLabel.size(), scoresPerFeature.size());
	    for (Pair<IntWritable,VectorWritable> entry : new SequenceFileDirIterable<IntWritable,VectorWritable>(
	        new Path(job1Path), PathType.LIST, PathFilters.partFilter(), conf)) {
	      scoresPerLabelAndFeature.assignRow(entry.getFirst().get(), entry.getSecond().get());
	    }

	    Vector perlabelThetaNormalizer = scoresPerLabel.like();
	    return new NaiveBayesModel(scoresPerLabelAndFeature, scoresPerFeature, scoresPerLabel, perlabelThetaNormalizer,
	        alphaI,false);
	}
	
}
