package bayes;


import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

/**
 * Created by linux on 17-3-18.
 */
public class Seq2sparse {

    public static void main(String[] args){
        try {
//            org.apache.lucene.analysis.core.WhitespaceAnalyzer/
            String[] strtest={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/seqdirtest",
                    "-o","/mahoutbayes/code/test-seq2parse",
                    "-a", "org.apache.lucene.analysis.core.WhitespaceAnalyzer",
                    "-lnorm",
                    "-nv",
                    "-ow",
                    "-wt","tfidf" //主要完成字典生成，给词编号，统计词频   转换乘向量模式
            };

            String[] strtrain={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/seqdirtrain",
                    "-o","/mahoutbayes/code/train-seq2parse",
                    "-a", "org.apache.lucene.analysis.core.WhitespaceAnalyzer",
                    "-lnorm",
                    "-nv",
                    "-ow",
                    "-wt","tfidf" //主要完成字典生成，给词编号，统计词频   转换乘向量模式
            };

            String[] test={"-fs", "hdfs://hadoop:9000",
                    "-i","/mahoutbayes/code/seqnewtest",
                    "-o","/mahoutbayes/code/newtestseq2parse",
                    "-a", "org.apache.lucene.analysis.core.WhitespaceAnalyzer",
                    "-lnorm",
                    "-nv",
                    "-ow",
                    "-wt","tfidf" //主要完成字典生成，给词编号，统计词频   转换乘向量模式
            };

//            SparseVectorsFromSequenceFiles.main(strtest);
//            SparseVectorsFromSequenceFiles.main(strtrain);
            SparseVectorsFromSequenceFiles.main(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
