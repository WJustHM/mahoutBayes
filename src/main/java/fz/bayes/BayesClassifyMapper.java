package fz.bayes;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.classifier.naivebayes.AbstractNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.StandardNaiveBayesClassifier;
import org.apache.mahout.math.Vector;

/**
 *  自定义Mapper，输出当前值和分类的结果
 * @author Administrator
 *
 */
@SuppressWarnings("deprecation")
public  class BayesClassifyMapper extends Mapper<LongWritable, Text, Text, Text>{
	private AbstractNaiveBayesClassifier classifier;
	private String SV;
	private Map<Integer, String> labelMap;
	private String labelIndex;
		@Override
	  public void setup(Context context) throws IOException, InterruptedException {
			
	    Configuration conf = context.getConfiguration();
	    Path modelPath = new Path(DistributedCache.getCacheFiles(conf)[0].getPath());
	    NaiveBayesModel model = NaiveBayesModel.materialize(modelPath, conf);
	    classifier = new StandardNaiveBayesClassifier(model);
	    SV = conf.get("SV");
	    labelIndex=conf.get("labelIndex");
		labelMap = BayesUtils.readLabelIndex(conf, new Path(labelIndex));
	  }

	  @Override
	  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		  String values =value.toString();
		  if("".equals(values)){
			  context.getCounter("Records", "Bad Record").increment(1);
			  return; 
		  }
		  String[] line = values.split(SV);
		  
		  Vector original =BayesUtil.transformToVector(line);
     	  Vector result = classifier.classifyFull(original);
     	  String label = BayesUtil.classifyVector(result, labelMap);
	    
	    //the key is the vector 
	    context.write(value, new Text(label));
	  }
}