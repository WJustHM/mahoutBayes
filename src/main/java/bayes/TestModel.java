package bayes;

import org.apache.mahout.classifier.naivebayes.test.TestNaiveBayesDriver;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;

/**
 * Created by linux on 17-3-19.
 */
public class TestModel {
    public static void main(String[] args){
        try {
            String[] strtest={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/test-seq2parse/tfidf-vectors",
                    "-m", "/mahoutbayes/code/train/model",				//	训练文物的输出文件
                    "-l", "/mahoutbayes/code/train/labelindex",				//	测试任务的输出文件
                    //	从输入文件中获得标识
                    "-o", "/mahoutbayes/code/testmodel/-testing",			//	输出文件
                    "-ow",
                    "-c",
                    "--tempDir","/s4"};

            String[] strtrain={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/train-seq2parse/tfidf-vectors",
                    "-m", "/mahoutbayes/code/train/model",				//	训练文物的输出文件
                    "-l", "/mahoutbayes/code/train/labelindex",				//	测试任务的输出文件
                    //	从输入文件中获得标识
                    "-o", "/mahoutbayes/code/testmodel/-testing",			//	输出文件
                    "-ow",
                    "-c",
                    "--tempDir","/s4"};

            TestNaiveBayesDriver.main(strtest);
//            TestNaiveBayesDriver.main(strtrain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
