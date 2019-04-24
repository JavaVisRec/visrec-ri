package visrec.ri.regression;

import deepnetts.data.DataSet;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.util.Tensor;

import javax.visrec.regression.SimpleLinearRegression;
import java.util.Map;

/**
 *
 * @author zoran
 */
public class DeepNettsSimpleLinearRegression extends SimpleLinearRegression<FeedForwardNetwork> {

    private float[] input = new float[1];
    private Tensor inputTensor = Tensor.create(1, 1, input);

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


    public static class Builder implements javax.visrec.util.Builder<DeepNettsSimpleLinearRegression> {
        private DeepNettsSimpleLinearRegression product = new DeepNettsSimpleLinearRegression();

        private float learningRate = 0.01f;
        private float maxError = 0.03f;
        private int maxEpochs = 1000;

        private DataSet<?> trainingSet; // replace with DataSet from visrec


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

        public Builder trainingSet(DataSet<?> trainingSet) {
            this.trainingSet = trainingSet;
            return this;
        }

        // test set
        // target accuracy

        @Override
        public DeepNettsSimpleLinearRegression build() {
            FeedForwardNetwork model= FeedForwardNetwork.builder()
                                        .addInputLayer(1)
                                        .addOutputLayer(1, ActivationType.LINEAR)
                                        .lossFunction(LossType.MEAN_SQUARED_ERROR)
                                        .build();

            BackpropagationTrainer trainer = new BackpropagationTrainer(model);
            trainer.setLearningRate(learningRate)
                    .setMaxError(0.005f);
            trainer.train(trainingSet);

            product.intercept = model.getOutputLayer().getBiases()[0];
            product.slope = model.getOutputLayer().getWeights().get(0);

            product.setModel(model);
            return product;
        }

        public DeepNettsSimpleLinearRegression build(Map prop) {
            // set properties from prop
            // iterate properties and set corresponding attributes using reflection - can be default method
//            for(String propName : prop.keySet()) {
//
//            }

            return build();
        }


    }
}
