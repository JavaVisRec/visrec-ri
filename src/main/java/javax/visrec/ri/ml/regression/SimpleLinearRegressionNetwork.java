package javax.visrec.ri.ml.regression;

import deepnetts.data.MLDataItem;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.util.Tensor;

import javax.visrec.ml.regression.SimpleLinearRegression;
import javax.visrec.ml.data.DataSet;

/**
 * Simple linear regression implemented Feed Forward Neural Network as a back-end.
 * 
 * @see SimpleLinearRegression
 */
public class SimpleLinearRegressionNetwork extends SimpleLinearRegression<FeedForwardNetwork> {

    private final float[] input = new float[1];
    private final Tensor inputTensor = Tensor.create(1, 1, input);

    private float slope;
    private float intercept;

    @Override
    public Float predict(Float inputs) {
      input[0] = inputs;
      FeedForwardNetwork ffn = getModel();
      ffn.setInput(inputTensor);
      float[] output = ffn.getOutput();
      return output[0];
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public float getSlope() {
        return slope;
    }

    @Override
    public float getIntercept() {
        return intercept;
    }


    public static class Builder implements javax.visrec.ml.model.ModelBuilder<SimpleLinearRegressionNetwork> {
        private SimpleLinearRegressionNetwork buildingBlock = new SimpleLinearRegressionNetwork();

        private float learningRate = 0.01f;
        private float maxError = 0.03f;
        private int maxEpochs = 1000;

        private DataSet<? extends MLDataItem> trainingSet; // replace with DataSet from visrec


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
        public SimpleLinearRegressionNetwork build() {
            FeedForwardNetwork model= FeedForwardNetwork.builder()
                                        .addInputLayer(1)
                                        .addOutputLayer(1, ActivationType.LINEAR)
                                        .lossFunction(LossType.MEAN_SQUARED_ERROR)
                                        .build();

            BackpropagationTrainer trainer = new BackpropagationTrainer(model);
            trainer.setLearningRate(learningRate)
                    .setMaxError(maxError)
                    .setMaxEpochs(maxEpochs);
            trainer.train(trainingSet);

            buildingBlock.intercept = model.getOutputLayer().getBiases()[0];
            buildingBlock.slope = model.getOutputLayer().getWeights().get(0);

            buildingBlock.setModel(model);
            return buildingBlock;
        }


    }
}
