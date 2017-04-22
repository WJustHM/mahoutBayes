package fz.bayes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.AbstractNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.StandardNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.training.WeightsMapper;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.math.Vector;
/**
 * ????????Job
 * ???
 * [
 *   2.1,3.2,1.2
	 2.1,3.2,1.3
   ]
   ??????????§Ù???(???????????????)
 * @author fansy
 *
 */
public class BayesClassifiedJob extends AbstractJob {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new BayesClassifiedJob(),args);
	}
	
	@Override
	public int run(String[] args) throws Exception {
		addInputOption();
	    addOutputOption();
	    addOption("model","m", "The file where bayesian model store ");
	    addOption("labelIndex","labelIndex", "The file where the index store ");
	    addOption("labelNumber","ln", "The labels number ");
	    addOption("mapreduce","mr", "Whether use mapreduce, true use ,else not use ");
	    addOption("SV","SV","The input vector splitter ,default is comma",",");
	    
	    if (parseArguments(args) == null) {
		      return -1;
		}
	    Configuration conf=getConf();
	    Path input = getInputPath();
	    Path output = getOutputPath();
	    String labelNumber=getOption("labelNumber");
	    String modelPath=getOption("model");
	    String useMR = getOption("mapreduce");
	    String SV = getOption("SV");
	    String labelIndex = getOption("labelIndex");
	    int returnCode=-1;
	    if("true".endsWith(useMR)){
	    	returnCode = useMRToClassify(conf,labelNumber,modelPath,input,output,SV,labelIndex);
	    }else{
	    	returnCode = classify(conf,input, output, labelNumber, modelPath, SV, labelIndex);
	    }
	    return returnCode;
	}
	/**
	 * ??????
	 * @param conf
	 * @param input
	 * @param output
	 * @param labelNumber
	 * @param modelPath
	 * @param sv
	 * @param labelIndex
	 * @return
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	private int classify(Configuration conf, Path input ,Path output ,String labelNumber,String modelPath,
			String sv,String labelIndex)  {
		// ????????? 
		try{
		NaiveBayesModel model = NaiveBayesModel.materialize(new Path(modelPath), conf);
		 AbstractNaiveBayesClassifier classifier = new StandardNaiveBayesClassifier(model);
		 Map<Integer, String> labelMap = BayesUtils.readLabelIndex(conf, new Path(labelIndex));
		 Path outputPath =new Path(output,"result");
		 // ???§Ø?????????????????§Õ??????????
		 FileSystem fs =FileSystem.get(input.toUri(),conf);
		 FSDataInputStream in=fs.open(input);  
		 
	     InputStreamReader istr=new InputStreamReader(in);  
	     BufferedReader br=new BufferedReader(istr);  
	     if(fs.exists(outputPath)){
	    	 fs.delete(outputPath, true);
	     }
	     FSDataOutputStream out = fs.create(outputPath);
	     
	     String lines;
	     StringBuffer buff = new StringBuffer();
	     while((lines=br.readLine())!=null&&!"".equals(lines)){  
	    	 String[] line = lines.toString().split(sv);
	    	 if(line.length<1){
	    		 break;
	    	 }
			  Vector original =BayesUtil.transformToVector(line);
        	  Vector result = classifier.classifyFull(original);
        	  String label = BayesUtil.classifyVector(result, labelMap);
        	  buff.append(lines+sv+label+"\n");
//        	 out.writeUTF(lines+sv+label);
//        	 out.
	     }
	     out.writeUTF(buff.substring(0, buff.length()-1));
	     out.flush();
	     out.close();
	     br.close();
	     istr.close();
	     in.close();
//	     fs.close();
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
/**
 * MR ??
 * @param conf
 * @param labelNumber
 * @param modelPath
 * @param input
 * @param output
 * @param SV
 * @param labelIndex
 * @return
 * @throws IOException
 * @throws ClassNotFoundException
 * @throws InterruptedException
 */
	private int useMRToClassify(Configuration conf, String labelNumber, String modelPath, Path input, Path output, 
			String SV, String labelIndex) throws IOException, ClassNotFoundException, InterruptedException {
		
	    conf.set(WeightsMapper.class.getName() + ".numLabels",labelNumber);
	    conf.set("SV", SV);
	    conf.set("labelIndex", labelIndex);
	    HadoopUtil.cacheFiles(new Path(modelPath), conf);
	    HadoopUtil.delete(conf, output);
	    Job job=Job.getInstance(conf, "");
	    job.setJobName("Use bayesian model to classify the  input:"+input.getName());
	    job.setJarByClass(BayesClassifiedJob.class); 
	    
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	    
	    job.setMapperClass(BayesClassifyMapper.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(Text.class);
	    job.setNumReduceTasks(0);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    FileInputFormat.setInputPaths(job, input);
	    FileOutputFormat.setOutputPath(job, output);
	    
	    if(job.waitForCompletion(true)){
	    	return 0;
	    }
		return -1;
	}
	
	
	
}

