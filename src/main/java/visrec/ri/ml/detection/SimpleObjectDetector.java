package visrec.ri.ml.detection;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.visrec.AbstractImageClassifier;
import javax.visrec.util.BoundingBox;

/**
 *
 * @author Zoran Sevarac <zoran.sevarac@deepnetts.com>
 */
public class SimpleObjectDetector extends AbstractObjectDetector<BufferedImage> {

    private double threshold = 0.5;

    public SimpleObjectDetector(AbstractImageClassifier<BufferedImage, Boolean> classifier) {
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
     * @param image
     * @return
     */
    @Override
    public Map<String, List<BoundingBox>> detectObject(BufferedImage image) {
        Map<String, List<BoundingBox>> results = new HashMap<>();

        int boxWidth = 64, boxHeight = 64;

        for (int y = 0; y < image.getHeight() - boxHeight; y++) {
            for (int x = 0; x < image.getWidth() - boxWidth; x++) {

                Map<String, Float> results2 = getImageClassifier().classify(image.getSubimage(x, y, boxWidth, boxHeight));
                for (Map.Entry<String, Float> keyValPair : results2.entrySet()) {
                    if (keyValPair.getValue() > threshold) {
                        BoundingBox bbox = new BoundingBox(keyValPair.getKey(), keyValPair.getValue(), x, y, boxWidth, boxHeight);
                        // results.add(bbox);
                    }
                }
            }
        }

        return results;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
