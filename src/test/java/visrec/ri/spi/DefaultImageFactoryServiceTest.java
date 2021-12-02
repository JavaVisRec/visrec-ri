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
