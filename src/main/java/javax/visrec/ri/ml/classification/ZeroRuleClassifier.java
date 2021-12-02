/**
 * Visual Recognition API for Java, JSR381
 * Copyright (C) 2020  Zoran Sevarac, Frank Greco
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package javax.visrec.ri.ml.classification;

import deepnetts.data.MLDataItem;
import java.util.HashMap;
import java.util.Map;
import javax.visrec.ml.classification.Classifier;
import javax.visrec.ml.data.BasicDataSet;
import javax.visrec.ml.data.DataSet;


/**
 * Zero rule classifier always returns as a result the most frequent class from the data set.
 * It is used for benchmarking purposes: if a model performs worse than that, then it is useless.
 */
public class ZeroRuleClassifier<T, R> implements Classifier<T, Map<R, Float>> {

    R mostFrequentClass;
    
    @Override
    public Map<R, Float> classify(T input) {
        Map<R, Float> map = new HashMap<>(); 
        map.put(mostFrequentClass, 1.0f);
        return map;
    }
    
    public static ZeroRuleClassifierBuilder builder() {
        return new ZeroRuleClassifierBuilder();
    }
    

    public static class ZeroRuleClassifierBuilder<T> {
        ZeroRuleClassifier buildingBlock;
        
        
        public ZeroRuleClassifierBuilder() {
            buildingBlock = new ZeroRuleClassifier();
        }
        
        public ZeroRuleClassifierBuilder trainingSet(DataSet<MLDataItem> dataSet) {
            int[] targetClassCount = new int[dataSet.get(0).getTargetOutput().size()]; // dataSet.getTargetNames().length
            // iterate entire data set
            for(MLDataItem ml : dataSet.getItems()) {
                float[] cols = ml.getTargetOutput().getValues(); // get output/target columns
                for(int i=0; i<cols.length; i++) {
                    if (cols[i]==1) targetClassCount[i]++; // assume they are one hot encoded
                }
            }
            
            // get index of class with max frequencey (occurance)
            int maxIdx = indexOfMax(targetClassCount);
            
            // get actual class (for Deep Netts it's a String)
            String mostFrequentClazz = ((BasicDataSet)dataSet).getTargetColumnsNames()[maxIdx];
            buildingBlock.mostFrequentClass = mostFrequentClazz;
                               
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
