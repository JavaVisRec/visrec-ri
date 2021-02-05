package javax.visrec.ri.ml.classification;

import deepnetts.net.FeedForwardNetwork;

import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.ml.model.ModelProvider;

/**
 * Implementation of a classifier using Feed Forward neural network in background for binary classification tasks.
 */
public class FeedForwardNetBinaryClassifier implements ModelProvider<FeedForwardNetwork>, NeuralNetBinaryClassifier<float[]> {

    private final FeedForwardNetwork model;

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

}
