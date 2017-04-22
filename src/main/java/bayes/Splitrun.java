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
//                    "-tr", "/djj/tmp/split/-train-vectors",				//	ѵ�����������ļ�
//                    "-te", "/djj/tmp/split/-test-vectors",				//	�������������ļ�
//                    "-rp","40",					//	ʹ��MRģʽʱ����ȡ������Ϊ�������ݵİٷֱ�
//                    "-ow",
//                    "--sequenceFiles",
//                    "-xm", "sequential",//	�㷨ִ��ģʽ�ֲ�ʽ���ǵ���
//                    "--tempDir","/s3"};
            String[] str={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/test-seq2parse/tfidf-vectors",
                    "-tr", "/mahoutbayes/split/-train-vectors",				//	ѵ�����������ļ�
                    "-te", "/mahoutbayes/split/-test-vectors",				//	�������������ļ�
                    "-rp","40",					//	ʹ��MRģʽʱ����ȡ������Ϊ�������ݵİٷֱ�
                    "-ow",
                    "--sequenceFiles",
                    "-xm", "sequential",//	�㷨ִ��ģʽ�ֲ�ʽ���ǵ���
                    "--tempDir","/s2"};
            SplitInput.main(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
