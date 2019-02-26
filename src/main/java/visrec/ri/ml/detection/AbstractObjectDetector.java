package visrec.ri.ml.detection;

import javax.visrec.AbstractImageClassifier;
import javax.visrec.ml.detection.ObjectDetector;
import javax.visrec.util.BoundingBox;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * missing image to search in
 *
 * @author Zoran Sevarac zoran.sevarac@deepnetts.com
 */
public abstract class AbstractObjectDetector<IMAGE_CLASS> implements ObjectDetector<IMAGE_CLASS> {

    AbstractImageClassifier<IMAGE_CLASS, Boolean> imageClassifier; // This should be binary classifier, that can detectObject some object / image

    public AbstractObjectDetector(AbstractImageClassifier<IMAGE_CLASS, Boolean> imageClassifier) {
        this.imageClassifier = imageClassifier;
    }

    /**
     * Scan entire image and return positions where object is detected
     *
     * @param image image to scan for objects
     * @return
     */
    @Override
    public abstract Map<String, List<BoundingBox>> detectObject(IMAGE_CLASS image);

    public Map<String, List<BoundingBox>> detect(File file) throws IOException {
        IMAGE_CLASS image = imageClassifier.getImageFactory().getImage(file);
        return detectObject(image);
    }

    public Map<String, List<BoundingBox>> detect(InputStream inStream) throws IOException {
        IMAGE_CLASS image = imageClassifier.getImageFactory().getImage(inStream);
        return detectObject(image);
    }

    /**
     * Subclasses should use ths method to use the inderlying image classifier
     *
     * @return returns classifier used for detection
     */
    public AbstractImageClassifier<IMAGE_CLASS, Boolean> getImageClassifier() {
        return imageClassifier;
    }

}
