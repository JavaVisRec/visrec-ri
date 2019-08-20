package visrec.ri.ml.classification;

import deepnetts.data.ExampleImage;
import deepnetts.data.ImageSet;
import deepnetts.net.ConvolutionalNetwork;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.opt.OptimizerType;
import deepnetts.util.DeepNettsException;
import deepnetts.util.FileIO;

import javax.visrec.AbstractImageClassifier;
import javax.visrec.util.VisRecConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of abstract image classifier for BufferedImage-s using
 * Convolutional network form Deep Netts.
 *
 * @author Zoran Sevarac
 */
public class DeepNettsImageClassifier extends AbstractImageClassifier<BufferedImage, ConvolutionalNetwork> {

    // it seems that these are not used at the end, onlz in builder. Do we need them exposed here__
    private int inputWidth, inputHeight;

    public static final Logger LOGGER = Logger.getLogger(DeepNettsImageClassifier.class.getName());

    public DeepNettsImageClassifier() {
        super(BufferedImage.class);
    }

    @Override
    public Map<String, Float> classify(BufferedImage sample) {
        // create input for neural network from image
        ExampleImage exImage = new ExampleImage(sample);

        // get underlying ML model, in this case convolutional network
        ConvolutionalNetwork neuralNet = getModel();
        // set neural network input and get outputs
        neuralNet.setInput(exImage.getInput());
        float[] outputs = neuralNet.getOutput();

        // get all class labels with corresponding output larger then classification threshold
        Map<String, Float> results = new HashMap<>();
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > getThreshold()) {
                results.put(neuralNet.getOutputLabel(i), outputs[i]); // if : threshold_
            }
        }

        return results;
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public int getInputHeight() {
        return inputHeight;
    }

    // static builder method for this class
    public static javax.visrec.util.Builder<DeepNettsImageClassifier> builder() {
        return new Builder();
    }

    public static class Builder implements javax.visrec.util.Builder<DeepNettsImageClassifier> {

        DeepNettsImageClassifier dnImgClassifier = new DeepNettsImageClassifier();

        @Override
        public DeepNettsImageClassifier build() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public DeepNettsImageClassifier build(Map<String, Object> config) {
            int imageWidth = Integer.parseInt(String.valueOf(config.get(VisRecConstants.IMAGE_WIDTH)));
            int imageHeight = Integer.parseInt(String.valueOf(config.get(VisRecConstants.IMAGE_HEIGHT)));
            String labelsFile = String.valueOf(config.get(VisRecConstants.LABELS_FILE));
            String trainingFile = String.valueOf(config.get(VisRecConstants.TRAINING_FILE));
            float maxError = Float.parseFloat(String.valueOf(config.get(VisRecConstants.SGD_MAX_ERROR)));
            int maxEpochs = Integer.parseInt(String.valueOf(config.get(VisRecConstants.SGD_MAX_EPOCHS)));
            float learningRate = Float.parseFloat(String.valueOf(config.get(VisRecConstants.SGD_LEARNING_RATE)));
            // subSampleSize u procentima ili tacan broj?
            // invert zero mean

            String saveToFile = String.valueOf(config.get(VisRecConstants.MODEL_SAVE_TO));

            ImageSet imageSet = new ImageSet(imageWidth, imageHeight);
            LOGGER.info("Loading images...");

            imageSet.loadLabels(new File(labelsFile));
            try {
                imageSet.loadImages(new File(trainingFile), false, 1000); // paths in training file should be relative
                imageSet.invert();
                //  imageSet.zeroMean();
                imageSet.shuffle();
            } catch (DeepNettsException ex) {
                java.util.logging.Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

            LOGGER.info("Done!");
            LOGGER.info("Creating neural network...");

            String modelJsonFile = String.valueOf(config.get("visrec.model.deepnetts"));
            ConvolutionalNetwork neuralNet = null;
            try {
                neuralNet = (ConvolutionalNetwork) FileIO.createFromJson(new File(modelJsonFile));
                neuralNet.setOutputLabels(imageSet.getOutputLabels());
            } catch (IOException ex) {
                Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

            LOGGER.info("Training neural network");

            neuralNet.setOutputLabels(imageSet.getOutputLabels());

            // create a set of convolutional networks and do training, crossvalidation and performance evaluation
            BackpropagationTrainer trainer = neuralNet.getTrainer();
            trainer.setLearningRate(learningRate)
                    .setMaxError(maxError)
                    .setMaxEpochs(maxEpochs)
                    .setBatchMode(false)
                    .setOptimizer(OptimizerType.SGD);
            trainer.train(imageSet);

            dnImgClassifier.setModel(neuralNet);

            try {
                FileIO.writeToFile(neuralNet, saveToFile);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }

            return dnImgClassifier;

        }

    }

}
