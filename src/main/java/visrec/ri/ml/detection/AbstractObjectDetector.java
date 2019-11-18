package visrec.ri.ml.detection;

import javax.visrec.AbstractImageClassifier;
import javax.visrec.ml.ClassificationException;
import javax.visrec.ml.detection.ObjectDetector;
import javax.visrec.util.BoundingBox;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract object detector which implements {@link ObjectDetector} to return the positions
 * of an object within the given image.
 *
 * @author Zoran Sevarac
 */
public abstract class AbstractObjectDetector implements ObjectDetector<BufferedImage> {

    private AbstractImageClassifier<Boolean> imageClassifier; // This should be binary classifier, that can detectObject some object / image

    /**
     * Creates an instance of the object detector
     * @param imageClassifier A {@link AbstractImageClassifier} which may not be null
     */
    public AbstractObjectDetector(AbstractImageClassifier<Boolean> imageClassifier) {
        Objects.requireNonNull(imageClassifier, "A classifier is required for the object detector.");
        this.imageClassifier = imageClassifier;
    }

    /**
     * Scan entire image and return positions where object is detected
     *
     * @param image {@code IMAGE_CLASS} image
     * @return {@code Map} of {@link BoundingBox} of where the object
     * has been detected.
     */
    @Override
    public abstract Map<String, List<BoundingBox>> detectObject(BufferedImage image) throws ClassificationException;

    /**
     * Detect the object based on the given {@code File}.
     * @param file Image file.
     * @return {@code Map} of {@link BoundingBox} of where the object
     * has been detected.
     * @throws IOException if the image couldn't be retrieved from storage.
     */
    public Map<String, List<BoundingBox>> detect(File file) throws IOException, ClassificationException {
        BufferedImage image = imageClassifier.getImageFactory().getImage(file);
        return detectObject(image);
    }

    /**
     * Detect the object based on the given {@code InputStream}.
     * @param inStream {@code InputStream} of the image
     * @return {@code Map} of {@link BoundingBox} of where the object
     * has been detected.
     * @throws IOException if the image couldn't be retrieved from storage.
     */
    public Map<String, List<BoundingBox>> detect(InputStream inStream) throws IOException, ClassificationException {
        BufferedImage image = imageClassifier.getImageFactory().getImage(inStream);
        return detectObject(image);
    }

    /**
     * Subclasses should use ths method to use the underlying image classifier
     *
     * @return configured {@link AbstractImageClassifier} of the {@link AbstractObjectDetector}
     */
    public AbstractImageClassifier<Boolean> getImageClassifier() {
        return imageClassifier;
    }

}
