package visrec.ri.ml.classification;

import javax.visrec.ml.classification.Classifier;
import javax.visrec.ml.classification.ClassifierBuilder;

/**
 * @author Kevin Berendsen
 */
public final class DefaultClassifierBuilder extends ClassifierBuilder {

    /** {@inheritDoc} */
    @Override
    public ClassifierBuilder trainedModel(Object trainedModel) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /** {@inheritDoc} */
    @Override
    public <T, R> Classifier<T, R> buildWithSourceType(Class<T> sourceCls, Class<R> returnCls) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
