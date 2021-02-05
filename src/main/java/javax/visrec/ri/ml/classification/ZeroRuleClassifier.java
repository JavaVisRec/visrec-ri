package javax.visrec.ri.ml.classification;

import deepnetts.data.MLDataItem;
import java.util.Map;
import javax.visrec.ml.classification.Classifier;
import javax.visrec.ml.data.DataSet;


/**
 * Zero rule classifier always returns as a result the most frequent class from the data set.
 * It is used for benchmarking purposes: if a model performs worse than that, then it is useless.
 * 
 */
public class ZeroRuleClassifier<T, R> implements Classifier<T, Map<String, Float>> {

    String mostFrequentClass;
    
    @Override
    public Map<String, Float> classify(T input) {
        return Map.of(mostFrequentClass, 1.0f);
    }
    
    // TODO: Add builder
    // builder chould simply cont the occurance of all possible classes in the data set

    public static ZeroRuleClassifierBuilder builder() {
        return new ZeroRuleClassifierBuilder();
    }

    public static class ZeroRuleClassifierBuilder<T> {
        ZeroRuleClassifier buildingBlock;
        
        
        public ZeroRuleClassifierBuilder() {
            buildingBlock = new ZeroRuleClassifier();
        }
        
        public ZeroRuleClassifierBuilder trainingSet(DataSet<MLDataItem> dataSet) {
            int[] classCount = new int[dataSet.get(0).getTargetOutput().size()]; // dataSet.getTargetNames().length
            for(MLDataItem ml : dataSet.getItems()) {
                float[] cols = ml.getTargetOutput().getValues();
                for(int i=0; i<cols.length; i++) {
                    if (cols[i]==1) classCount[i]++;
                }
            }
            
            int maxIdx = indexOfMax(classCount);
            //ZeroRuleClassifier.this.mostFrequentClass =  dataSet.getTargetNames()[maxIdx];
            // uzmo koa je to klasa

            // count occurancies of each class
            // how to know which attibutes are classes. are thay targets? golumns MLDataSetItem
            // napravi ovde brojac
            
                // kako da izbrojim klase sa obektm o 
                // moram da znam tip elemenata u dat asetu - ali ovaj korisiti deep netts implemetaciju?            
              //dataSet.getTargetNames();
              //dataSet.getColumnNames();
          //    dataSet.getTargetColumns();
          //    dataSet.columnAt(4).setAsTarget(true);
              //dataSet.setTargetColumns(4, 5, 6);
              // cognitive load of a programming line - nuber of concept, understandability
                    
            return this;
        }

        private int indexOfMax(int[] classCount) {
            int max = classCount[0], maxIdx=0;
            for(int i=1; i<classCount.length; i++) {
                if (classCount[i] > max) {
                    max = classCount[i];
                    maxIdx = i;
                }
            }
            return maxIdx;
        }
        
    }    
}
