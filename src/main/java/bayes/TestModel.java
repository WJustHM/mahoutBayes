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
                    "-m", "/mahoutbayes/code/train/model",				//	ѵ�����������ļ�
                    "-l", "/mahoutbayes/code/train/labelindex",				//	�������������ļ�
                    //	�������ļ��л�ñ�ʶ
                    "-o", "/mahoutbayes/code/testmodel/-testing",			//	����ļ�
                    "-ow",
                    "-c",
                    "--tempDir","/s4"};

            String[] strtrain={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/train-seq2parse/tfidf-vectors",
                    "-m", "/mahoutbayes/code/train/model",				//	ѵ�����������ļ�
                    "-l", "/mahoutbayes/code/train/labelindex",				//	�������������ļ�
                    //	�������ļ��л�ñ�ʶ
                    "-o", "/mahoutbayes/code/testmodel/-testing",			//	����ļ�
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
