package bayes;

import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;
import org.apache.mahout.utils.SplitInput;

import java.util.Arrays;

/**
 * Created by linux on 17-3-19.
 */
public class train {
    public static void main(String[] args) {
        try {
//            String[] str={
//                    "-i","/djj/tmp/split/-train-vectors",
//                    "-o", "/djj/tmp/train/model",				//	训练文物的输出文件
//                    "-li", "/djj/tmp/train/labelindex",				//	测试任务的输出文件
//                    //	从输入文件中获得标识
//                    "-ow"};
            String[] str = {"-fs", "hdfs://hadoop:9000",
                    "-i", "/mahoutbayes/code/train-seq2parse/tfidf-vectors",
                    "-o", "/mahoutbayes/code/train/model",                //	训练文物的输出文件
                    "-li", "/mahoutbayes/code/train/labelindex",                //	测试任务的输出文件
                    "-c",
                    "-ow",
                    "--tempDir", "/s3"};
            TrainNaiveBayesJob.main(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
