package visrec.ri.spi;

import visrec.ri.BufferedImageFactory;

import javax.visrec.ImageFactory;
import javax.visrec.spi.ImageFactoryService;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Default implementation of {@link ImageFactoryService} which serves the implementations of {@link ImageFactory}.
 *
 * @author Kevin Berendsen
 */
public final class DefaultImageFactoryService implements ImageFactoryService {

    private static final Map<Class<?>, ImageFactory<?>> imageFactories;
    static {
        imageFactories = new HashMap<>();
        imageFactories.put(BufferedImage.class, new BufferedImageFactory());
    }

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
        Objects.requireNonNull(imageCls, "imageCls == null");
        ImageFactory<?> imageFactory = imageFactories.get(imageCls);
        return Optional.ofNullable((ImageFactory<T>) imageFactory);
    }
}
