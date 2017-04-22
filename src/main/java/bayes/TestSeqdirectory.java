package bayes;

import org.apache.mahout.text.SequenceFilesFromDirectory;
/**
 * Created by linux on 17-3-14.
 */
public class TestSeqdirectory {
    public static void main(String[] args) {

        String[] argtest = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/��ҵ���test",
                "-o", "/mahoutbayes/code/seqdirtest",
                "--tempDir", "/st1"};

        String[] argtrain = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/��ҵ���train",
                "-o", "/mahoutbayes/code/seqdirtrain",
                "--tempDir", "/st2"};

        String[] test = {"-fs", "hdfs://hadoop:9000",
                "-ow",
                "-i", "/mahoutbayes/data/����",
                "-o", "/mahoutbayes/code/seqnewtest",
                "--tempDir", "/st2"};
        try {
//            SequenceFilesFromDirectory.main(argtest);
//            SequenceFilesFromDirectory.main(argtrain);
            SequenceFilesFromDirectory.main(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
