package visrec.ri.ml.classification;

import deepnetts.data.ExampleImage;
import deepnetts.data.ImageSet;
import deepnetts.net.ConvolutionalNetwork;
import deepnetts.net.layers.SoftmaxOutputLayer;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.CrossEntropyLoss;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.opt.OptimizerType;
import deepnetts.util.DeepNettsException;

import javax.visrec.AbstractImageClassifier;
import javax.visrec.util.VisRecConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * TODO: Traffic sign recognition healhcare - skin, radiology caner for bone -
 * MRI vegetarian or non vegeterian food, transportation boxes simple
 * implementation and ditributed implemantayion using th esame api as hazelcast
 * trab what is teh biggest challange for javadeveloper starting tio use it
 * apache or gpl wit classpath , allow dynamic linkoing
 *
 * separate training from using. (resource usage) 3d stereo vision - using conv
 * networks use configuration JSR apache 382 iso standardize containers thermal
 * imaging
 *
 * BIG QUESTION HOW WILL WE INJECT ML MODEL? OR HOW WILL WE PROVIDE PARAMETERS
 * FOR BUILDING INTERNAL MODEL? WE SHOULD BE ABLE TO DO BOTH: incejt model or
 * build internal model using standard, or customized settings
 *
 * @author Zoran Sevarac
 */
public class DeepNettsImageClassifier extends AbstractImageClassifier<BufferedImage, ConvolutionalNetwork> {

    private int inputWidth, inputHeight;

    public static final Logger LOGGER = Logger.getLogger(DeepNettsImageClassifier.class.getName());

    public DeepNettsImageClassifier() {
        super(BufferedImage.class);
    }

    @Override
    public Map<String, Float> classify(BufferedImage sample) {
        Map<String, Float> results = new HashMap<>();
        ConvolutionalNetwork neuralNet = getModel();

        ExampleImage exImage = new ExampleImage(sample);
        neuralNet.setInput(exImage.getInput());
        neuralNet.forward();

        float[] outputs = neuralNet.getOutput();

        for (int i = 1; i < outputs.length; i++) {
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

    public static javax.visrec.util.Builder<DeepNettsImageClassifier> builder() {
        return new Builder();
    }

    public static class Builder implements javax.visrec.util.Builder<DeepNettsImageClassifier> {

        DeepNettsImageClassifier dnImgClassifier = new DeepNettsImageClassifier();

        @Override
        public DeepNettsImageClassifier build() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        // how to specify network archittecture which is a graph abesed model?
        //use json object? specific Configuration? it has to be graph ike structure
        // json file or json object or cprecific configuration
        @Override
        public DeepNettsImageClassifier build(Map<String, Object> config) {
            int imageWidth = Integer.parseInt(String.valueOf(config.get(VisRecConstants.IMAGE_WIDTH)));
            int imageHeight = Integer.parseInt(String.valueOf(config.get(VisRecConstants.IMAGE_HEIGHT)));
            String labelsFile = String.valueOf(config.get(VisRecConstants.LABELS_FILE));
            String trainingFile = String.valueOf(config.get(VisRecConstants.TRAINING_FILE));
            float maxError = Float.parseFloat(String.valueOf(config.get(VisRecConstants.SGD_MAX_ERROR)));
            float learningRate = Float.parseFloat(String.valueOf(config.get(VisRecConstants.SGD_LEARNING_RATE)));

            String modelFile = String.valueOf(config.get("visrec.model.saveToFile"));

            ImageSet imageSet = new ImageSet(imageWidth, imageHeight);
            LOGGER.info("Loading images...");

            imageSet.loadLabels(new File(labelsFile));
            try {
                imageSet.loadImages(new File(trainingFile), true); // paths in training file should be relative
                imageSet.invert();
                imageSet.zeroMean();
                imageSet.shuffle();
            } catch (DeepNettsException ex) {
                java.util.logging.Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
            }

            int classCount = imageSet.getLabelsCount();

            LOGGER.info("Done!");
            LOGGER.info("Creating neural network...");

//        String modelJsonFile = prop.getProperty("visrec.model.deepnetts");
//        ConvolutionalNetwork neuralNet = null;
//        try {
//            neuralNet = FileIO.createFromJson(new File(modelJsonFile));
//            //neuralNet.setOutputLabels(imageSet.getLabels());
//        } catch (IOException ex) {
//            Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
//        }

            //get architecture from json instead of this hardcoding
            ConvolutionalNetwork neuralNet = new ConvolutionalNetwork.Builder()
                    .addInputLayer(imageWidth, imageHeight, 3)
                    .addConvolutionalLayer(5, 5, 3, ActivationType.RELU)
                    .addMaxPoolingLayer(2, 2, 2)
                    .addConvolutionalLayer(3, 3, 6, ActivationType.RELU)
                    .addMaxPoolingLayer(2, 2, 2)
              //      .addFullyConnectedLayer(30, ActivationType.RELU)
                    .addFullyConnectedLayer(20, ActivationType.RELU)
                    .addOutputLayer(classCount, SoftmaxOutputLayer.class)
                    .lossFunction(CrossEntropyLoss.class)
                    .randomSeed(123)
                    .build();

            LOGGER.info("Done!");
            LOGGER.info("Training neural network");

            neuralNet.setOutputLabels(imageSet.getOutputLabels());

            // create a set of convolutional networks and do training, crossvalidation and performance evaluation
            BackpropagationTrainer trainer = new BackpropagationTrainer(neuralNet);
            trainer.setLearningRate(learningRate)
                    .setMomentum(0.7f)
                    .setMaxError(maxError)
                    .setBatchMode(false)
                    .setOptimizer(OptimizerType.MOMENTUM);
            trainer.train(imageSet);

            dnImgClassifier.setModel(neuralNet);

//            try {
//                FileIO.writeToFile(neuralNet, modelFile);
//            } catch (IOException ex) {
//                java.util.logging.Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
//            }

            return dnImgClassifier;

        }

    }

}
