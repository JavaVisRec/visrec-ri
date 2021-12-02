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

package visrec.ri.util;

import org.junit.jupiter.api.Test;

import javax.visrec.ml.model.ModelCreationException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import javax.visrec.ml.model.ModelBuilder;

/**
 * @author Kevin Berendsen
 */
public class BuilderConfigurationTest {

    /**
     * Successfully build the output of the builder.
     */
    @Test
    public void testReflectionInvocationBuild() throws ModelCreationException {
        Map<String, String> trainingSet = new HashMap<>();
        trainingSet.put("hello", "world");
        trainingSet.put("lorem", "ipsum");
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("trainingSet", trainingSet);

        BuilderImpl builder = new BuilderImpl();
        String output = builder.build(configuration);
        assertEquals("{lorem=ipsum, hello=world}", output);
    }

    /**
     * The trainingSet method is invoked without the valid parameter and should
     * throw an exception.
     */
    @Test
    public void testInvalidParameterForMethod() {
        String trainingSet = "invalid";
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("trainingSet", trainingSet);

        BuilderImpl builder = new BuilderImpl();
        try {
            builder.build(configuration);
            fail("The configuration is invalid and should throw the InvalidBuilderConfigurationException");
        } catch (ModelCreationException e) {
            /* Expected */
        }
    }



    public static class BuilderImpl implements ModelBuilder<String> {

        private Map<String, String> trainingSet;

        public void trainingSet(Map<String, String> trainingSet) {
            this.trainingSet = trainingSet;
        }

        @Override
        public String build() {
            return trainingSet.toString();
        }
    }

}
