package fz.bayes;

import java.util.Map;

import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class BayesUtil {

	/**
	 * 把输入字符串转换为Vector
	 * @param lines
	 * @return
	 */
	public static Vector transformToVector(String[] line){
		Vector v=new RandomAccessSparseVector(line.length);
		for(int i=0;i<line.length;i++){
			double item=0;
			try{
				item=Double.parseDouble(line[i]);
			}catch(Exception e){
				return null; // 如果不可以转换，说明输入数据有问题
			}
			v.setQuick(i, item);
		}
		return v;
	}
	/**
	 * 根据得分值分类
	 * @param v
	 * @param labelMap
	 * @return
	 */
	public static String classifyVector(Vector v,Map<Integer, String> labelMap){
		int bestIdx = Integer.MIN_VALUE;
		double bestScore = Long.MIN_VALUE;
		for (Vector.Element element : v.all()) {
			if (element.get() > bestScore) {
				bestScore = element.get();
				bestIdx = element.index();
			}
		}
		if (bestIdx != Integer.MIN_VALUE) {
			ClassifierResult classifierResult = new ClassifierResult(labelMap.get(bestIdx), bestScore);
			return classifierResult.getLabel();
		}
		
		return null;
	}
}
