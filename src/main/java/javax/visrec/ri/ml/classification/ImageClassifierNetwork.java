package javax.visrec.ri.ml.classification;

import deepnetts.data.ExampleImage;
import deepnetts.net.ConvolutionalNetwork;

import javax.visrec.ml.classification.AbstractImageClassifier;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of abstract image classifier for BufferedImage-s using
 * Convolutional network form Deep Netts.
 *
 * @author Zoran Sevarac
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
