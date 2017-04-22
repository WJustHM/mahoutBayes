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
	 * Bayesian �㷨�ĵ�����
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new BayesRunner(),args);
	}
	@Override
	public int run(String[] args) throws Exception {
		// ���ò���
		addInputOption();
	    addOutputOption();
	    // ��������֮��ķָ�����Ĭ��Ϊ���ţ�
	    addOption("splitCharacterVector","scv", "Vector split character,default is ','", ",");
	    // ���������ͱ�ʾ�ķָ�����Ĭ��Ϊð�ţ�
	    addOption("splitCharacterLabel","scl", "Vector and Label split character,default is ':'", ":");
	
	    if (parseArguments(args) == null) {
		      return -1;
		}
	    Path input = getInputPath();
	    Path output = getOutputPath();  // ��ʱ�ļ�����ɾȥ
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
	     * ת����������
	     */
	    System.out.println("***********************************ת�����ݿ�ʼ");
	    String[] transArgs=new String[]{"-i",input.toString(),"-o",tmpPath+TRANSFORM_OUTPUTPATH,"-scl",scl,"-scv",scv};
	    if(ToolRunner.run(new Configuration(), new TFText2VectorWritable(),transArgs)!=0){
	    	return -1;
	    }
	    
	    /*
	     * д��indexLabel����
	     */
	    System.out.println("***********************************д��indexLabel����ʼ");
	    long labelNumber=WriteIndexLabel.writeLabelIndex(tmpPath+TRANSFORM_OUTPUTPATH,
	    		output.toString()+LABELINDEX, conf);


	    /*
	     * ִ�� bayesJob1
	     */
	    System.out.println("***********************************BayesJob1��ʼִ��");
	    String[] job1Args=new String[]{"-i",tmpPath+TRANSFORM_OUTPUTPATH,
	    		"-li",tmpPath+LABELINDEX,"-o",tmpPath+JOB1PATH};
	    if(ToolRunner.run(new Configuration(), new BayesJob1(),job1Args)!=0){
	    	return -1;
	    }
	    
	    /*
	     * ִ�� bayesJob2
	     */
	    System.out.println("***********************************BayesJob2��ʼִ��");
	    String[] job2Args=new String[]{"-i",tmpPath+JOB1PATH,
	    		"-ln",String.valueOf(labelNumber),"-o",tmpPath+JOB2PATH};
	    if(ToolRunner.run(new Configuration(), new BayesJob2(),job2Args)!=0){
	    	return -1;
	    }
	    /*
	     * д��bayesian model����
	     */
	    System.out.println("***********************************д��bayesian model ����ʼ");
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
	     * ����ԭʼ��������
	     */  
	    System.out.println("***********************************��������ʼ");
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
	     * ��ӡ������Ϣ
	     */
	    System.out.println("***********************************��ӡ������Ϣ��ʼ");
	    String[] anaArg={
	    		
				"-i",tmpPath+BAYESCLASSIFIEDPATH,
				"-o","",
				"-li",tmpPath+LABELINDEX
				};
	    OperateArgsToolRunner.run(new AnalyzeBayesModel(),anaArg);
		return 0;
	}

	

}
