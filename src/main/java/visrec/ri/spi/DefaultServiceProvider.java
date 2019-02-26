package visrec.ri.spi;

import javax.visrec.spi.BuilderService;
import javax.visrec.spi.ClassifierService;
import javax.visrec.spi.ImageFactoryService;
import javax.visrec.spi.ImplementationService;
import javax.visrec.spi.ServiceProvider;

/**
 * @author Kevin Berendsen
 */
public final class DefaultServiceProvider extends ServiceProvider {

    /**
     * Get the {@link BuilderService}
     * @return {@link BuilderService}
     */
    @Override
    public BuilderService getBuilderService() {
        return new DefaultBuilderService();
    }

    /**
     * Get the {@link ClassifierService}
     * @return {@link ClassifierService}
     */
    @Override
    public ClassifierService getClassifierService() {
        return new DefaultClassifierService();
    }

    /**
     * Get the {@link ImageFactoryService}
     * @return {@link ImageFactoryService}
     */
    @Override
    public ImageFactoryService getImageFactoryService() {
        return new DefaultImageFactoryService();
    }

    @Override
    public ImplementationService getImplementationService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
