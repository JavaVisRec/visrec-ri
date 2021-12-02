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
