package bayes;

import org.apache.mahout.utils.SplitInput;

/**
 * Created by linux on 17-3-19.
 */
public class Splitrun{
    public static void main(String[] args){
        try {
//            String[] str={
//                    "-i","/djj/tmp/seq2parse/tfidf-vectors",
//                    "-tr", "/djj/tmp/split/-train-vectors",				//	训练文物的输出文件
//                    "-te", "/djj/tmp/split/-test-vectors",				//	测试任务的输出文件
//                    "-rp","40",					//	使用MR模式时，抽取出来作为测试数据的百分比
//                    "-ow",
//                    "--sequenceFiles",
//                    "-xm", "sequential",//	算法执行模式分布式还是单机
//                    "--tempDir","/s3"};
            String[] str={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/test-seq2parse/tfidf-vectors",
                    "-tr", "/mahoutbayes/split/-train-vectors",				//	训练文物的输出文件
                    "-te", "/mahoutbayes/split/-test-vectors",				//	测试任务的输出文件
                    "-rp","40",					//	使用MR模式时，抽取出来作为测试数据的百分比
                    "-ow",
                    "--sequenceFiles",
                    "-xm", "sequential",//	算法执行模式分布式还是单机
                    "--tempDir","/s2"};
            SplitInput.main(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
