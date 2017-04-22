package fz.bayes.model;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.HadoopUtil;

import fz.bayes.model.util.OperateArgsToolRunner;

import static fz.bayes.model.util.BayesUtil.*;
public class BayesRunner extends AbstractJob {

	/**
	 * Bayesian 算法的调用类
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new BayesRunner(),args);
	}
	@Override
	public int run(String[] args) throws Exception {
		// 设置参数
		addInputOption();
	    addOutputOption();
	    // 增加向量之间的分隔符，默认为逗号；
	    addOption("splitCharacterVector","scv", "Vector split character,default is ','", ",");
	    // 增加向量和标示的分隔符，默认为冒号；
	    addOption("splitCharacterLabel","scl", "Vector and Label split character,default is ':'", ":");
	
	    if (parseArguments(args) == null) {
		      return -1;
		}
	    Path input = getInputPath();
	    Path output = getOutputPath();  // 临时文件，先删去
	    String tmpPath=output.toString();
//	    String jt=getOption("jt",DEFAULT_JOBTRACKER);  //
//		String fs=getOption("fs",DEFAULT_FS);   //
	    Configuration conf=getConf();
//	    conf.set("mapred.job.tracker", jt);
	    HadoopUtil.delete(conf, output);
	    System.out.println("********************************************output directory "+output.toString()+" deleted");
	    String scv=getOption("splitCharacterVector");
	    String scl=getOption("splitCharacterLabel");
	    
	    /*
	     * 转换数据任务
	     */
	    System.out.println("***********************************转换数据开始");
	    String[] transArgs=new String[]{"-i",input.toString(),"-o",tmpPath+TRANSFORM_OUTPUTPATH,"-scl",scl,"-scv",scv};
	    if(ToolRunner.run(new Configuration(), new TFText2VectorWritable(),transArgs)!=0){
	    	return -1;
	    }
	    
	    /*
	     * 写入indexLabel任务
	     */
	    System.out.println("***********************************写入indexLabel任务开始");
	    long labelNumber=WriteIndexLabel.writeLabelIndex(tmpPath+TRANSFORM_OUTPUTPATH,
	    		output.toString()+LABELINDEX, conf);


	    /*
	     * 执行 bayesJob1
	     */
	    System.out.println("***********************************BayesJob1开始执行");
	    String[] job1Args=new String[]{"-i",tmpPath+TRANSFORM_OUTPUTPATH,
	    		"-li",tmpPath+LABELINDEX,"-o",tmpPath+JOB1PATH};
	    if(ToolRunner.run(new Configuration(), new BayesJob1(),job1Args)!=0){
	    	return -1;
	    }
	    
	    /*
	     * 执行 bayesJob2
	     */
	    System.out.println("***********************************BayesJob2开始执行");
	    String[] job2Args=new String[]{"-i",tmpPath+JOB1PATH,
	    		"-ln",String.valueOf(labelNumber),"-o",tmpPath+JOB2PATH};
	    if(ToolRunner.run(new Configuration(), new BayesJob2(),job2Args)!=0){
	    	return -1;
	    }
	    /*
	     * 写入bayesian model任务
	     */
	    System.out.println("***********************************写入bayesian model 任务开始");
	    String[] modelArg={
				"-i","",
				"-o","",
				"-mp",tmpPath+MODEL,
				"-bj1",tmpPath+JOB1PATH,
				"-bj2",tmpPath+JOB2PATH};
	    if(OperateArgsToolRunner.run(new WriteBayesModel(), modelArg)!=0){
	    	return -1;
	    }
	    /*
	     * 分类原始数据任务
	     */  
	    System.out.println("***********************************分类任务开始");
	    String[] classifyArgs=new String[]{
	    		
	    		"-i",tmpPath+TRANSFORM_OUTPUTPATH,
				"-m",tmpPath+MODEL,
				"-ln",String.valueOf(labelNumber),
				"-o",tmpPath+BAYESCLASSIFIEDPATH
	    };
	    
	    if(ToolRunner.run(conf, new BayesClassifyJob(),classifyArgs)!=0){
	    	return -1;
	    }
	    /*
	     * 打印测试信息
	     */
	    System.out.println("***********************************打印测试信息开始");
	    String[] anaArg={
	    		
				"-i",tmpPath+BAYESCLASSIFIEDPATH,
				"-o","",
				"-li",tmpPath+LABELINDEX
				};
	    OperateArgsToolRunner.run(new AnalyzeBayesModel(),anaArg);
		return 0;
	}

	

}
