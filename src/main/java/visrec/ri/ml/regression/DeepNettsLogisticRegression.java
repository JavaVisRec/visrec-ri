package visrec.ri.ml.regression;

import deepnetts.data.DeepNettsDataSetItem;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.util.Tensor;

import javax.visrec.ml.regression.LogisticRegression;
import java.util.HashMap;
import java.util.Map;
import javax.visrec.ml.data.DataSet;

/**
 *
 * @author zoran
 */
public class DeepNettsLogisticRegression extends LogisticRegression<FeedForwardNetwork> {

    @Override
    public Map<Boolean, Float> classify(float[] input) {
      FeedForwardNetwork model = getModel();
      model.setInput(Tensor.create(1, input.length, input)); //TODO: put array to input tensor placeholder
      float[] output = model.getOutput();
      Map<Boolean, Float> result = new HashMap<>();
      result.put(Boolean.TRUE, output[0]);
      result.put(Boolean.FALSE, 1-output[0]);
      return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    // TODO: add static builder class and method

   public static class Builder implements javax.visrec.util.Builder<DeepNettsLogisticRegression> {


        private float learningRate = 0.01f;
        private float maxError = 0.03f;
        private int maxEpochs = 1000;
        private int inputsNum;

        private DataSet<? extends DeepNettsDataSetItem> trainingSet; // replace with DataSet from visrec

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

        public Builder trainingSet(DataSet<? extends DeepNettsDataSetItem> trainingSet) {
            this.trainingSet = trainingSet;
            return this;
        }

        // test set
        // target accuracy

        @Override
        public DeepNettsLogisticRegression build() {
            FeedForwardNetwork model= FeedForwardNetwork.builder()
                                        .addInputLayer(inputsNum)
                                        .addOutputLayer(1, ActivationType.SIGMOID)
                                        .lossFunction(LossType.CROSS_ENTROPY)
                                        .build();

            BackpropagationTrainer trainer = new BackpropagationTrainer(model);
            trainer.setLearningRate(learningRate)
                    .setMaxEpochs(maxEpochs)
                    .setMaxError(maxError);

            if (trainingSet!=null)
                trainer.train(trainingSet);

            DeepNettsLogisticRegression product = new DeepNettsLogisticRegression();
            product.setModel(model);
            return product;
        }

    }
}
