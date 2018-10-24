package visrec.ri.spi;

import javax.visrec.ml.classification.Classifier;
import javax.visrec.spi.ClassifierService;

/**
 * @author Kevin Berendsen
 */
public final class DefaultClassifierService implements ClassifierService {
    /**
     * Creates a {@link Classifier} by source type and result map type.
     * @param sourceType {@link Class} object of the source type.
     * @param resultMapType {@link Class} object of the result map type
     * @param <T> source type
     * @param <R> result map type
     * @return {@link Classifier} found by source type and result map type.
     */
    @Override
    public <T, R> Classifier<T, R> getBySource(Class<T> sourceType, Class<R> resultMapType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
