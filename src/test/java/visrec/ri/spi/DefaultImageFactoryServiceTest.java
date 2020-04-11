package visrec.ri.spi;

import org.junit.jupiter.api.Test;

import javax.visrec.ImageFactory;
import javax.visrec.spi.ImageFactoryService;
import javax.visrec.spi.ServiceProvider;
import java.awt.image.BufferedImage;
import java.util.Optional;
import javax.visrec.ri.BufferedImageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests regarding the {@link DefaultImageFactoryService}
 * @author Kevin Berendsen
 */
public class DefaultImageFactoryServiceTest {

    /**
     * Test the instantiation of {@link BufferedImageFactory} through the {@link ImageFactoryService}
     */
    @Test
    public void testBufferedImageImageFactoryInstantiation() {
        Optional<ImageFactory<BufferedImage>> imageFactory = ServiceProvider.current().getImageFactoryService().getByImageType(BufferedImage.class);
        assertTrue(imageFactory.isPresent());
        // If the casting fails, the implementation is incorrect and it will fail the test.
        BufferedImageFactory.class.cast(imageFactory.get());
    }
}
