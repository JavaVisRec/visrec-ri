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
import javax.visrec.ml.detection.ObjectDetector;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract object detector which implements {@link ObjectDetector} to return the positions
 * of an object within the given image.
 */
public abstract class AbstractObjectDetector implements ObjectDetector<BufferedImage> {

    private AbstractImageClassifier<BufferedImage, Boolean> imageClassifier;

    /**
     * Creates an instance of the object detector
     *
     * @param imageClassifier A {@link AbstractImageClassifier} which may not be null
     */
    public AbstractObjectDetector(AbstractImageClassifier<BufferedImage, Boolean> imageClassifier) {
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
     *
     * @param path Image file.
     * @return {@code Map} of {@link BoundingBox} of where the object
     * has been detected.
     * @throws IOException             if the image couldn't be retrieved from storage.
     * @throws ClassificationException when the detector was unable to classify and detect the input
     */
    public Map<String, List<BoundingBox>> detect(Path path) throws IOException, ClassificationException {
        BufferedImage image = imageClassifier.getImageFactory().getImage(path);
        return detectObject(image);
    }

    /**
     * Detect the object based on the given {@code InputStream}.
     *
     * @param inStream {@code InputStream} of the image
     * @return {@code Map} of {@link BoundingBox} of where the object
     * has been detected.
     * @throws IOException             if the image couldn't be retrieved from storage.
     * @throws ClassificationException when the detector was unable to classify and detect the input
     */
    public Map<String, List<BoundingBox>> detect(InputStream inStream) throws IOException, ClassificationException {
        BufferedImage image = imageClassifier.getImageFactory().getImage(inStream);
        return detectObject(image);
    }

    /**
     * Subclasses should use this method to use the underlying image classifier
     *
     * @return configured {@link AbstractImageClassifier} of the {@link AbstractObjectDetector}
     */
    public AbstractImageClassifier<BufferedImage, Boolean> getImageClassifier() {
        return imageClassifier;
    }

}
