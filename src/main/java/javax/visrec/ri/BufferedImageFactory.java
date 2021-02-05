package javax.visrec.ri;

import javax.imageio.ImageIO;
import javax.visrec.ImageFactory;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

/**
 * {@link ImageFactory} to provide {@link BufferedImage} as return object.
 *
 * @author Kevin Berendsen
 */
public class BufferedImageFactory implements ImageFactory<BufferedImage> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getImage(Path path) throws IOException {
        BufferedImage img = ImageIO.read(path.toFile());
        if (img == null) {
            throw new IOException("Failed to transform Path into BufferedImage due to unknown image encoding");
        }
        return img;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getImage(URL file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IOException("Failed to transform URL into BufferedImage due to unknown image encoding");
        }
        return img;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getImage(InputStream file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            throw new IOException("Failed to transform InputStream into BufferedImage due to unknown image encoding");
        }
        return img;
    }
}
