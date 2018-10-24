package visrec.ri.spi;

import javax.visrec.ImageFactory;
import javax.visrec.spi.ImageFactoryService;
import java.util.Optional;

/**
 * @author Kevin Berendsen
 */
public final class DefaultImageFactoryService implements ImageFactoryService {
    /**
     * Get the {@link ImageFactory} by image type.
     * @param imageCls image type in {@link Class} object which is able to
     *                 be processed by the image factory implementation.
     * @param <T> image type.
     * @return {@link ImageFactory} wrapped in {@link Optional}. If the {@link ImageFactory} could not be
     * found then the {@link Optional} would contain null.
     */
    @Override
    public <T> Optional<ImageFactory<T>> getByImageType(Class<T> imageCls) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
