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

import deepnetts.data.ExampleImage;
import deepnetts.net.ConvolutionalNetwork;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of abstract image classifier for BufferedImage-s using
 * Convolutional network form Deep Netts.
 */
public class ImageClassifierNetwork extends AbstractImageClassifier<BufferedImage, ConvolutionalNetwork> {

    // it seems that these are not used at the end, onlz in builder. Do we need them exposed here__
    private int inputWidth, inputHeight;

    public ImageClassifierNetwork(ConvolutionalNetwork network) {
        super(BufferedImage.class, network);
    }

    @Override
    public Map<String, Float> classify(BufferedImage inputImage) {
        // create input for neural network from image
        ExampleImage exImage = new ExampleImage(inputImage);

        // get underlying ML model, in this case convolutional network
        ConvolutionalNetwork neuralNet = getModel();
        // set neural network input and get outputs
        neuralNet.setInput(exImage.getInput());
        float[] outputs = neuralNet.getOutput();

        // get all class labels with corresponding output larger then classification threshold
        Map<String, Float> results = new HashMap<>();
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] >= getThreshold())
                results.put(neuralNet.getOutputLabel(i), outputs[i]);
        }

        return results;
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public int getInputHeight() {
        return inputHeight;
    }

}
