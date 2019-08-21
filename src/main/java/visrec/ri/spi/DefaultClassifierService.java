package visrec.ri.spi;

import deepnetts.data.ImageSet;
import deepnetts.net.ConvolutionalNetwork;
import deepnetts.net.layers.SoftmaxOutputLayer;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.CrossEntropyLoss;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.opt.OptimizerType;
import deepnetts.util.DeepNettsException;
import visrec.ri.ml.classification.ImageClassifierNetwork;

import javax.visrec.ml.classification.Classifier;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.spi.ClassifierService;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kevin Berendsen
 */
public final class DefaultClassifierService implements ClassifierService {

    private static final Logger LOGGER = Logger.getLogger(DefaultClassifierService.class.getName());

    @Override
    public ImageClassifier createImageClassifier(ImageClassifier.BuildingBlock block) {
        ImageSet imageSet = new ImageSet(block.getImageWidth(), block.getImageWidth());
        LOGGER.info("Loading images...");

        imageSet.loadLabels(block.getLabelsFile());
        try {
            imageSet.loadImages(block.getTrainingsFile(), true); // paths in training file should be relative
            imageSet.invert();
            imageSet.zeroMean();
            imageSet.shuffle();
        } catch (DeepNettsException | FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
                .addInputLayer(block.getImageWidth(), block.getImageHeight(), 3)
                .addConvolutionalLayer(5, 5, 3, ActivationType.RELU)
                .addMaxPoolingLayer(2, 2, 2)
                .addConvolutionalLayer(3, 3, 6, ActivationType.RELU)
                .addMaxPoolingLayer(2, 2, 2)
                .addFullyConnectedLayer(30, ActivationType.RELU)
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
        trainer.setLearningRate(block.getLearningRate())
                .setMomentum(0.7f)
                .setMaxError(block.getMaxError())
                .setBatchMode(false)
                .setOptimizer(OptimizerType.MOMENTUM);
        trainer.train(imageSet);

        ImageClassifierNetwork imageClassifier = new ImageClassifierNetwork(neuralNet);

//            try {
//                FileIO.writeToFile(neuralNet, modelFile);
//            } catch (IOException ex) {
//                java.util.logging.Logger.getLogger(DeepNettsImageClassifier.class.getName()).log(Level.SEVERE, null, ex);
//            }

        return imageClassifier;
    }
}
