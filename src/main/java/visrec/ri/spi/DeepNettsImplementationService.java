package visrec.ri.spi;

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
    public int getMajorVersion() {
        return 1;
    }

    /** {@inheritDoc} */
    @Override
    public int getMinorVersion() {
        return 0;
    }
}
