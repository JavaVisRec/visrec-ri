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

package javax.visrec.ri.ml.regression;

import deepnetts.data.MLDataItem;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.util.Tensor;

import javax.visrec.ml.classification.LogisticRegression;
import javax.visrec.ml.data.DataSet;


/**
 * Logistic regresion algorithm implemented by neural network.
 */
public class LogisticRegressionNetwork extends LogisticRegression<FeedForwardNetwork> {

    @Override
    public Float classify(float[] input) {
        FeedForwardNetwork model = getModel();
        model.setInput(Tensor.create(1, input.length, input)); //TODO: put array to input tensor placeholder
        return model.getOutput()[0];
    }

    public static Builder builder() {
        return new Builder();
    }
    
    

    public static class Builder implements javax.visrec.ml.model.ModelBuilder<LogisticRegressionNetwork> {

        private float learningRate = 0.01f;
        private float maxError = 0.03f;
        private int maxEpochs = 1000;
        private int inputsNum;

        private DataSet<? extends MLDataItem> trainingSet; // replace with DataSet from visrec

        public Builder inputsNum(int inputsNum) {
            this.inputsNum = inputsNum;
            return this;
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

        public Builder trainingSet(DataSet<? extends MLDataItem> trainingSet) {
            this.trainingSet = trainingSet;
            return this;
        }

        // test set
        // target accuracy
        @Override
        public LogisticRegressionNetwork build() {
            FeedForwardNetwork model = FeedForwardNetwork.builder()
                    .addInputLayer(inputsNum)
                    .addOutputLayer(1, ActivationType.SIGMOID)
                    .lossFunction(LossType.CROSS_ENTROPY)
                    .build();

            BackpropagationTrainer trainer = new BackpropagationTrainer(model);
            trainer.setLearningRate(learningRate)
                    .setMaxEpochs(maxEpochs)
                    .setMaxError(maxError);

            if (trainingSet != null) {
                trainer.train(trainingSet);
            }

            LogisticRegressionNetwork product = new LogisticRegressionNetwork();
            product.setModel(model);
            return product;
        }

    }
}
