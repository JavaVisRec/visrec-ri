package visrec.ri.spi;

import javax.visrec.ml.classification.ClassifierBuilder;
import javax.visrec.spi.BuilderService;

/**
 * @author Kevin Berendsen
 */
public final class DefaultBuilderService implements BuilderService {
    /**
     * Creates a new {@link ClassifierBuilder}
     * @return {@link ClassifierBuilder}
     */
    @Override
    public ClassifierBuilder newClassifierBuilder() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
