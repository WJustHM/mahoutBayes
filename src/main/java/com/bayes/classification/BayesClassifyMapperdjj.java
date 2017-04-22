package com.bayes.classification;

import fz.bayes.BayesUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;
import org.apache.mahout.classifier.naivebayes.AbstractNaiveBayesClassifier;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.classifier.naivebayes.NaiveBayesModel;
import org.apache.mahout.classifier.naivebayes.StandardNaiveBayesClassifier;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.Map;

/**
 * Created by linux on 17-4-7.
 */
@SuppressWarnings("deprecation")
public class BayesClassifyMapperdjj extends Mapper<Text, VectorWritable, Text, Text> {
    Logger log=Logger.getLogger(BayesClassifyMapperdjj.class);
    private AbstractNaiveBayesClassifier classifier;
    private Map<Integer, String> labelMap;
    private String labelIndex;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {

        Configuration conf = context.getConfiguration();
        Path modelPath = HadoopUtil.getSingleCachedFile(conf);
        NaiveBayesModel model = NaiveBayesModel.materialize(modelPath, conf);
        classifier = new StandardNaiveBayesClassifier(model);

        labelIndex = conf.get("labelIndex");
        log.info("-----------+++++++++++++labelIndex:"+labelIndex);
        labelMap = BayesUtils.readLabelIndex(conf, new Path(labelIndex));
    }

    @Override
    public void map(Text key, VectorWritable value, Context context) throws IOException, InterruptedException {
        Vector result = classifier.classifyFull(value.get());

        String label = BayesUtil.classifyVector(result, labelMap);
        log.info("------------"+label);

        //the key is the vector
        context.write(new Text(key.toString()), new Text(label));
    }
}
