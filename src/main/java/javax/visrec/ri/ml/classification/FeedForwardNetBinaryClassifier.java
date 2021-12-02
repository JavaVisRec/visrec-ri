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

package javax.visrec.ri.ml.classification;

import deepnetts.net.FeedForwardNetwork;

import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.ml.model.ModelProvider;

/**
 * Implementation of a classifier using Feed Forward neural network in background for binary classification tasks.
 */
public class FeedForwardNetBinaryClassifier implements ModelProvider<FeedForwardNetwork>, NeuralNetBinaryClassifier<float[]> {

    private final FeedForwardNetwork model;
    private float threshold;    

    public FeedForwardNetBinaryClassifier(FeedForwardNetwork model) {
        this.model = model;
    }

    @Override
    public FeedForwardNetwork getModel() {
        return model;
    }

    public static NeuralNetBinaryClassifier.Builder<?> builder() {
        return NeuralNetBinaryClassifier.builder();
    }

    @Override
    public Float classify(float[] inputs) {
        model.setInput(inputs);
        return model.getOutput()[0];
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
    
    

}
