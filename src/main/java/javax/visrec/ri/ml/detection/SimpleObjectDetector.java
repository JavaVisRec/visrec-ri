/**
 * Visual Recognition API for Java, JSR381
 * Copyright (C) 2020  Zoran Sevarac, Frank Greco
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package javax.visrec.ri.ml.detection;

import javax.visrec.ri.ml.classification.AbstractImageClassifier;
import javax.visrec.ml.classification.ClassificationException;
import javax.visrec.ml.detection.BoundingBox;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple object detector.
 *
 */
public class SimpleObjectDetector extends AbstractObjectDetector {

    private double threshold = 0.5;

    /**
     * Creates an instance
     *
     * @param classifier A {@link AbstractImageClassifier} which may not be null
     */
    public SimpleObjectDetector(AbstractImageClassifier<BufferedImage, Boolean> classifier) {
        super(classifier);
    }

    /**
     * Scan image using brute force sliding window and return positions where
     * classifier returns score greater then threshold.
     * <p>
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
     *
     * @return theshold as {@code double}
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Set the threshold
     *
     * @param threshold as {@code double}
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
