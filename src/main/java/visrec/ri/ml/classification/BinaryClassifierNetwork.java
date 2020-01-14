package visrec.ri.ml.classification;

import deepnetts.data.DeepNettsDataSetItem;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;

import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.data.DataSet;
import javax.visrec.util.ModelProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of a classifier using Feed Forward neural network in background for binary classification tasks.
 * 
 * @author Zoran
 */
public class BinaryClassifierNetwork implements ModelProvider<FeedForwardNetwork>, BinaryClassifier<float[]> {

    private FeedForwardNetwork model;

    public BinaryClassifierNetwork(FeedForwardNetwork model) {
        this.model = model;
    }
            
    @Override
    public FeedForwardNetwork getModel() {
        return model;
    }

    @Deprecated
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Map<String, Float> classify(float[] inputs) {
        model.setInput(inputs);
        Map<String, Float> output = new HashMap<>();
        float[] outputValues = model.getOutput();
        for (int i = 0; i < outputValues.length; i++) {
            output.put(model.getOutputLabel(i), outputValues[i]);
        }
        return output;
    }

    @Deprecated
    public static class Builder implements javax.visrec.util.Builder<BinaryClassifierNetwork> {

        private float learningRate = 0.01f;
        private float maxError = 0.03f;
        private int maxEpochs = 1000;
        private int inputsNum;
        private int[] hiddenLayers;

        private DataSet<? extends DeepNettsDataSetItem> trainingSet; // replace with DataSet from visrec

        public Builder inputsNum(int inputsNum) {
            this.inputsNum = inputsNum;
            return this;
        }

        public Builder learningRate(double learningRate) {
            this.learningRate = (float)learningRate;
            return this;
        }

        public Builder maxError(double maxError) {
            this.maxError = (float)maxError;
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
        
        public Builder hiddenLayers(int... hiddenLayers) {
            this.hiddenLayers = hiddenLayers;
            return this;
        }
                                   
        @Override
        public BinaryClassifierNetwork build() {
           FeedForwardNetwork.Builder ffnBuilder = FeedForwardNetwork.builder();
           ffnBuilder.addInputLayer(inputsNum);
           
           for (int h : hiddenLayers) {
            ffnBuilder.addFullyConnectedLayer(h);
           }
                   
           ffnBuilder.addOutputLayer(1, ActivationType.SIGMOID)
                      .lossFunction(LossType.CROSS_ENTROPY);  
           
                   
           FeedForwardNetwork ffn = ffnBuilder.build();
           ffn.getTrainer().setMaxEpochs(maxEpochs)
                           .setMaxError(maxError)
                           .setLearningRate(learningRate);
           ffn.train(trainingSet);

           return new BinaryClassifierNetwork(ffn);
        }
        
    }


    
}
