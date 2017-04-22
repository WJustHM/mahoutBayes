package bayes;

import fz.bayes.BayesClassifiedJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by linux on 17-4-6.
 */
public class TestBayesClassifiedJobdjj {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        String[] strtrain = {
//                "-i", "hdfs://hadoop:9000/mahoutbayes/code/test-seq2parse/tfidf-vectors",
                "-i", "hdfs://hadoop:9000/mahoutbayes/code/newtestseq2parse/tfidf-vectors",
                "-o", "hdfs://hadoop:9000/mahoutbayes/code/testmodel/-testing",
                "-m", "hdfs://hadoop:9000/mahoutbayes/code/train/model",                //	训练文物的输出文件
                "-labelIndex", "hdfs://hadoop:9000/mahoutbayes/code/train/labelindex"             //	测试任务的输出文件
        };

        ToolRunner.run(conf, new BayesClassifiedJobdjj(), strtrain);
    }

}
