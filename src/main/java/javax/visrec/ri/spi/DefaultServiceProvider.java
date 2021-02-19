package javax.visrec.ri.spi;

import javax.visrec.spi.ImageFactoryService;
import javax.visrec.spi.ImplementationService;
import javax.visrec.spi.ServiceProvider;

/**
 * Default {@link ServiceProvider} of the implementation of the visual recognition API
 *
 */
public final class DefaultServiceProvider extends ServiceProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageFactoryService getImageFactoryService() {
        return new DefaultImageFactoryService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImplementationService getImplementationService() {
        return new DeepNettsImplementationService();
    }

}
