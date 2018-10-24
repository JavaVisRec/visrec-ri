package visrec.ri.spi;

import visrec.ri.ml.classification.DefaultClassifierBuilder;

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
        return new DefaultClassifierBuilder();
    }
}
