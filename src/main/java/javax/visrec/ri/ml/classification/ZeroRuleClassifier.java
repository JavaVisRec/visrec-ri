package javax.visrec.ri.ml.classification;

import java.util.Map;
import javax.visrec.ml.classification.Classifier;


public class ZeroRuleClassifier<T, R> implements Classifier<T, Map<R, Float>> {

    @Override
    public Map<R, Float> classify(T input) {
        // always return the most frequent class     
        return null;
    }
    
    // TODO: Add builder



}
