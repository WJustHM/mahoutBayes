package com.bayes.classification;

import bayes.BayesClassifiedJobdjj;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by linux on 17-4-6.
 */
public class TestBayesClassifiedJobdjj {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        String[] strtrain = {
                "-i", "hdfs://hadoop:9000/" + args[0],
                "-o", "hdfs://hadoop:9000/mahoutbayes/code/testmodel/-testing",
                "-m", "hdfs://hadoop:9000/mahoutbayes/train/train" + args[1] + "/model",                //	训练文物的输出文件
                "-labelIndex", "hdfs://hadoop:9000/mahoutbayes/train/train" + args[1] + "/labelindex"             //	测试任务的输出文件
        };

        ToolRunner.run(conf, new BayesClassifiedJobdjj(), strtrain);
    }

}
