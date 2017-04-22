package com.bayes.modeltrain;

import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by linux on 17-3-19.
 */
public class train {
    public static void main(String[] args) {
        try {
            String[] str1 = {"-fs", "hdfs://hadoop:9000",
                    "-i", "/mahoutbayes/train/train-seq2parse1/tfidf-vectors",
                    "-o", "/mahoutbayes/train/train1/model",                //	训练文物的输出文件
                    "-li", "/mahoutbayes/train/train1/labelindex",                //	测试任务的输出文件
                    "-c",
                    "-ow",
                    "--tempDir", "/s3"};
            String[] str2 = {"-fs", "hdfs://hadoop:9000",
                    "-i", "/mahoutbayes/train/train-seq2parse2/tfidf-vectors",
                    "-o", "/mahoutbayes/train/train2/model",                //	训练文物的输出文件
                    "-li", "/mahoutbayes/train/train2/labelindex",                //	测试任务的输出文件
                    "-c",
                    "-ow",
                    "--tempDir", "/s3"};
            String[] str3 = {"-fs", "hdfs://hadoop:9000",
                    "-i", "/mahoutbayes/train/train-seq2parse3/tfidf-vectors",
                    "-o", "/mahoutbayes/train/train3/model",                //	训练文物的输出文件
                    "-li", "/mahoutbayes/train/train3/labelindex",                //	测试任务的输出文件
                    "-c",
                    "-ow",
                    "--tempDir", "/s3"};

            TrainNaiveBayesJob.main(str1);
            TrainNaiveBayesJob.main(str2);
            TrainNaiveBayesJob.main(str3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
