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

import deepnetts.data.MLDataItem;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.util.Tensor;

import javax.visrec.ml.classification.AbstractMultiClassClassifier;
import java.util.HashMap;
import java.util.Map;
import javax.visrec.ml.data.DataSet;

public class MultiClassClassifierNetwork extends AbstractMultiClassClassifier<FeedForwardNetwork, float[], String> {

    @Override
    public Map<String, Float> classify(float[] input) {
      FeedForwardNetwork model = getModel();
      model.setInput(Tensor.create(1, input.length, input)); //TODO: put array to input tensor placeholder
      float[] outputs = model.getOutput();
      String[] labels = model.getOutputLabels();
      Map<String, Float> result = new HashMap<>();
      for(int i=0; i<outputs.length; i++) {
          result.put(labels[i], outputs[i]);
      }
      return result;
    }

    public static Builder builder() {
        return new Builder();
    }

   public static class Builder implements javax.visrec.ml.model.ModelBuilder<MultiClassClassifierNetwork> {
        private MultiClassClassifierNetwork building = new MultiClassClassifierNetwork();

        private float learningRate = 0.01f;
        private float maxError = 0.03f;
        private long maxEpochs = Long.MAX_VALUE;
        private int inputsNum;
        private int outputsNum;
        private int[] hiddenLayers;

        private DataSet<? extends MLDataItem>  trainingSet;

        @Override
        public MultiClassClassifierNetwork build() {
            // Network architecture as Map/properties, json?
            FeedForwardNetwork.Builder builder =  FeedForwardNetwork.builder()
                                                    .addInputLayer(inputsNum);
            for(int h : hiddenLayers) {
                builder.addFullyConnectedLayer(h, ActivationType.TANH);
            }

            builder.addOutputLayer(outputsNum, ActivationType.SOFTMAX)
                    .lossFunction(LossType.CROSS_ENTROPY)
                    .hiddenActivationFunction(ActivationType.TANH);

            FeedForwardNetwork model = builder.build();

            // aslo can be replaced with model.getTrainer()
            BackpropagationTrainer trainer = new BackpropagationTrainer(model); // model as param in constructor
            trainer.setLearningRate(learningRate)
                    .setMaxError(maxError)
                    .setMaxEpochs(maxEpochs);

            if (trainingSet!=null)
                trainer.train(trainingSet); // move model to constructor

            building.setModel(model);

            return building;
        }

        public Builder learningRate(float learningRate) {
            this.learningRate = learningRate;
            return this;
        }

        public Builder maxError(float maxError) {
            this.maxError = maxError;
            return this;
        }

        public Builder maxEpochs(int maxEpochs) {
            this.maxEpochs = maxEpochs;
            return this;
        }

        public Builder inputsNum(int inputsNum) {
            this.inputsNum = inputsNum;
            return this;
        }

        public Builder outputsNum(int outputsNum) {
            this.outputsNum = outputsNum;
            return this;
        }

        public Builder hiddenLayers(int... hiddenLayers) {
            this.hiddenLayers = hiddenLayers;
            return this;
        }

        public Builder trainingSet(DataSet<? extends MLDataItem>  trainingSet) {
            this.trainingSet = trainingSet;
            return this;
        }


   }
}
