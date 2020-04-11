package javax.visrec.ri.spi;

import javax.visrec.spi.ImplementationService;

/**
 * DeepNetts' {@link ImplementationService}
 * @author Kevin Berendsen
 */
public class DeepNettsImplementationService extends ImplementationService {

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "DeepNetts";
    }

    /** {@inheritDoc} */
    @Override
    public String getVersion() {
        return "1.1";
    }
}
