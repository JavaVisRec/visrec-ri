package javax.visrec.ri.spi;

import javax.visrec.spi.*;

/**
 * Default {@link ServiceProvider} of the implementation of the visual recognition API
 * @author Kevin Berendsen
 */
public final class DefaultServiceProvider extends ServiceProvider {

    /** {@inheritDoc} */
    @Override
    public BuilderService getBuilderService() {
        return null;//new DefaultBuilderService();
    }

    /** {@inheritDoc} */
    @Override
    public ImageFactoryService getImageFactoryService() {
        return new DefaultImageFactoryService();
    }

    /** {@inheritDoc} */
    @Override
    public ImplementationService getImplementationService() {
        return new DeepNettsImplementationService();
    }

}
