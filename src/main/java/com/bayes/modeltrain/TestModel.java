package com.bayes.modeltrain;

import org.apache.mahout.classifier.naivebayes.test.TestNaiveBayesDriver;

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
                    "--tempDir","/s5"};

            String[] strtrain={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/test/test-seq2parse1/tfidf-vectors",
                    "-m", "/mahoutbayes/train/train1/model",				//	训练文物的输出文件
                    "-l", "/mahoutbayes/train/train1/labelindex",				//	测试任务的输出文件
                    //	从输入文件中获得标识
                    "-o", "/mahoutbayes/code/testmodel/-testing",			//	输出文件
                    "-ow",
                    "-c",
                    "--tempDir","/s4"};

            TestNaiveBayesDriver.main(strtrain);
//            TestNaiveBayesDriver.main(strtrain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
