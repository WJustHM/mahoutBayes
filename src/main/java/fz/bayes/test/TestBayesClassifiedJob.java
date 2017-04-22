package fz.bayes.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import fz.bayes.BayesClassifiedJob;

public class TestBayesClassifiedJob {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://node33:8020");  
	    conf.set("mapreduce.framework.name", "yarn");  
	    conf.set("yarn.resourcemanager.address", "node33:8032");
	    String [] arg =new String[]{
	    		"-i","hdfs://node33:8020/user/root/bayes_nolabel",
	    		"-o","hdfs://node33:8020/user/root/bayes_label",
	    		"-m","hdfs://node33:8020/user/root/bayes/output001/model",
	    		"-labelIndex","hdfs://node33:8020/user/root/bayes/output001/labelIndex.bin",
	    		"-mr","false",
	    		"-SV",","
	    };
	    
	    ToolRunner.run(conf, new BayesClassifiedJob(),arg);
	}
}
