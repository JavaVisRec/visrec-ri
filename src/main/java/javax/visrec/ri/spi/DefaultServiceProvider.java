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

import javax.visrec.spi.ImageFactoryService;
import javax.visrec.spi.ImplementationService;
import javax.visrec.spi.ServiceProvider;

/**
 * Default {@link ServiceProvider} of the implementation of the visual recognition API
 *
 */
public final class DefaultServiceProvider extends ServiceProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageFactoryService getImageFactoryService() {
        return new DefaultImageFactoryService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImplementationService getImplementationService() {
        return new DeepNettsImplementationService();
    }

}
