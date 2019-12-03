package visrec.ri.ml.detection;

import javax.visrec.AbstractImageClassifier;
import javax.visrec.ml.ClassificationException;
import javax.visrec.util.BoundingBox;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple object detector
 *
 * @author Zoran Sevarac
 */
public class SimpleObjectDetector extends AbstractObjectDetector {

    private double threshold = 0.5;

    /**
     * Creates an instance
     * @param classifier A {@link AbstractImageClassifier} which may not be null
     */
    public SimpleObjectDetector(AbstractImageClassifier<Boolean> classifier) {
        super(classifier);
    }

    /**
     * Scan image using brute force sliding window and return positions where
     * classifier returns score greater then threshold.
     *
     * get width and height of the image and scan image with classifier - apply
     * classifier to each position This is trivial implementation and should be
     * replaced with something better
     *
     * @param image {@code BufferedImage} to scan
     * @return A {@code Map} of {@link BoundingBox} which contain
     * the positions of the detected object.
     */
    @Override
    public Map<String, List<BoundingBox>> detectObject(BufferedImage image) throws ClassificationException {
        Map<String, List<BoundingBox>> results = new HashMap<>();

        int boxWidth = 64, boxHeight = 64;

        for (int y = 0; y < image.getHeight() - boxHeight; y++) {
            for (int x = 0; x < image.getWidth() - boxWidth; x++) {

                Map<String, Float> results2 = getImageClassifier().classify(image.getSubimage(x, y, boxWidth, boxHeight));
                for (Map.Entry<String, Float> keyValPair : results2.entrySet()) {
                    if (keyValPair.getValue() > threshold) {
                        BoundingBox bbox = new BoundingBox(keyValPair.getKey(), keyValPair.getValue(), x, y, boxWidth, boxHeight);
                        //results.put(keyValPair.getKey(), bboxes); add these to list
                    }
                }
            }
        }

        return results;
    }

    /**
     * Get the threshold
     * @return theshold as {@code double}
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Set the threshold
     * @param threshold as {@code double}
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
