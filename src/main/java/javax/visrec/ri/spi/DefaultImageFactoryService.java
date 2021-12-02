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

package javax.visrec.ri.spi;

import javax.visrec.ri.BufferedImageFactory;

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
