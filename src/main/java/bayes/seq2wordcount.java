package bayes;

import org.apache.mahout.vectorizer.DictionaryVectorizer;

/**
 * Created by linux on 17-3-18.
 */
public class seq2wordcount {
    public static void main(String[] args){
        try {
            String[] str={
                    "-i","/djj/tmp/seq2parse/tokenized-documents/",
                    "-o","/djj/tmp/seq2parsewordcount",
            };
            DictionaryVectorizer.main(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
