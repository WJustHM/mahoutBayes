package com.bayes.modeltrain;

import org.apache.mahout.text.SequenceFilesFromDirectory;

/**
 * Created by linux on 17-3-14.
 */
public class TestSeqdirectory {
    public static void main(String[] args) {

        String[] argtest1 = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/Testdataclean/test1",
                "-o", "/mahoutbayes/test/seqdirtest1",
                "--tempDir", "/st1"};
        String[] argtest2 = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/Testdataclean/test2",
                "-o", "/mahoutbayes/test/seqdirtest2",
                "--tempDir", "/st1"};
        String[] argtest3 = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/Testdataclean/test3",
                "-o", "/mahoutbayes/test/seqdirtest3",
                "--tempDir", "/st1"};

        String[] argtrain1 = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/Trainingdataclean/train1",
                "-o", "/mahoutbayes/train/seqdirtrain1",
                "--tempDir", "/st2"};
        String[] argtrain2 = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/Trainingdataclean/train2",
                "-o", "/mahoutbayes/train/seqdirtrain2",
                "--tempDir", "/st2"};
        String[] argtrain3 = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/Trainingdataclean/train3",
                "-o", "/mahoutbayes/train/seqdirtrain3",
                "--tempDir", "/st2"};
        try {
            SequenceFilesFromDirectory.main(argtest1);
            SequenceFilesFromDirectory.main(argtest2);
            SequenceFilesFromDirectory.main(argtest3);
            SequenceFilesFromDirectory.main(argtrain1);
            SequenceFilesFromDirectory.main(argtrain2);
            SequenceFilesFromDirectory.main(argtrain3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
