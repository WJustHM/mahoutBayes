package fz.bayes.model;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class TFText2VectorWritable extends AbstractJob {
	/**
	 * �����
	 * [2.1,3.2,1.2:a
	 * 2.1,3.2,1.3:b]
	 * ����������ת��Ϊ key:new Text(a),value:new VectorWritable(2.1,3.2,1.2:a) ����������
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new TFText2VectorWritable(),args);
	}
	@Override
	public int run(String[] args) throws Exception {
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
	    Path output = getOutputPath();
	    String scv=getOption("splitCharacterVector");
	    String scl=getOption("splitCharacterLabel");
	    Configuration conf=getConf();
	//    FileSystem.get(output.toUri(), conf).deleteOnExit(output);//���������ڣ�ɾ�����
	    HadoopUtil.delete(conf, output);
	    conf.set("SCV", scv);
	    conf.set("SCL", scl);
	    Job job=new Job(conf);
	    job.setJobName("transform text to vector by input:"+input.getName());
	    job.setJarByClass(TFText2VectorWritable.class); 
	    
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    
	    job.setMapperClass(TFMapper.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(VectorWritable.class);
	    job.setNumReduceTasks(0);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(VectorWritable.class);
	    TextInputFormat.setInputPaths(job, input);
	    SequenceFileOutputFormat.setOutputPath(job, output);
	   
	   
	    if(job.waitForCompletion(true)){
	    	return 0;
	    }
		return -1;
	}

	
	public static class TFMapper extends Mapper<LongWritable,Text,Text,VectorWritable>{
		private String SCV;
		private String SCL;
		/**
		 * ��ʼ���ָ������� 
		 */
		@Override
		public void setup(Context ctx){
			SCV=ctx.getConfiguration().get("SCV");
			SCL=ctx.getConfiguration().get("SCL");
		}
		/**
		 * �����ַ����������
		 * @throws InterruptedException 
		 * @throws IOException 
		 */
		@Override
		public void map(LongWritable key,Text value,Context ctx) throws IOException, InterruptedException{
			String[] valueStr=value.toString().split(SCL);
			if(valueStr.length!=2){
				return;  // û������˵����������,�˳�
			}
			String name=valueStr[1];
			String[] vector=valueStr[0].split(SCV);
			Vector v=new RandomAccessSparseVector(vector.length);
			for(int i=0;i<vector.length;i++){
				double item=0;
				try{
					item=Double.parseDouble(vector[i]);
				}catch(Exception e){
					return; // ���������ת����˵����������������
				}
				v.setQuick(i, item);
			}
			NamedVector nv=new NamedVector(v,name);
			VectorWritable vw=new VectorWritable(nv);
			ctx.write(new Text(name), vw);
		}
		
	}
}
