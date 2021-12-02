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

import deepnetts.data.MLDataItem;
import deepnetts.data.TabularDataSet;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;

import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.model.ModelCreationException;
import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.ri.ml.classification.FeedForwardNetBinaryClassifier;
import javax.visrec.ri.util.DataSets;
import javax.visrec.spi.BinaryClassifierFactory;
import java.io.IOException;

public class FloatArrayBinaryClassifierFactory implements BinaryClassifierFactory<float[]> {

    @Override
    public Class<float[]> getTargetClass() {
        return float[].class;
    }

    @Override
    public BinaryClassifier<float[]> create(NeuralNetBinaryClassifier.BuildingBlock<float[]> block) throws ModelCreationException {
        FeedForwardNetwork.Builder ffnBuilder = FeedForwardNetwork.builder();
        ffnBuilder.addInputLayer(block.getInputsNum());

        for (int h : block.getHiddenLayers()) {
            ffnBuilder.addFullyConnectedLayer(h);
        }

        ffnBuilder.addOutputLayer(1, ActivationType.SIGMOID)
                .lossFunction(LossType.CROSS_ENTROPY);

        FeedForwardNetwork ffn = ffnBuilder.build();
        ffn.getTrainer()
                .setMaxEpochs(block.getMaxEpochs())
                .setMaxError(block.getMaxError())
                .setLearningRate(block.getLearningRate());

        TabularDataSet<MLDataItem> trainingSet = null;
        try {
            trainingSet = DataSets.readCsv(block.getTrainingPath().toFile(), block.getInputsNum(), 1, true, ",");
            //deepnetts.data.DataSets.normalizeMax(trainingSet);
        } catch (IOException e) {
            throw new ModelCreationException("Failed to create training set based on training file", e);
        }
        ffn.train(trainingSet);
        FeedForwardNetBinaryClassifier ffnbc = new FeedForwardNetBinaryClassifier(ffn);
        ffnbc.setThreshold(block.getThreshold());
        
        return ffnbc;
    }
}
