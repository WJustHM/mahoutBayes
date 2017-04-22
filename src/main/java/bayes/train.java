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
//                    "-o", "/djj/tmp/train/model",				//	ѵ�����������ļ�
//                    "-li", "/djj/tmp/train/labelindex",				//	�������������ļ�
//                    //	�������ļ��л�ñ�ʶ
//                    "-ow"};
            String[] str = {"-fs", "hdfs://hadoop:9000",
                    "-i", "/mahoutbayes/code/train-seq2parse/tfidf-vectors",
                    "-o", "/mahoutbayes/code/train/model",                //	ѵ�����������ļ�
                    "-li", "/mahoutbayes/code/train/labelindex",                //	�������������ļ�
                    "-c",
                    "-ow",
                    "--tempDir", "/s3"};
            TrainNaiveBayesJob.main(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
