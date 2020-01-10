package visrec.ri.spi;

import deepnetts.data.ImageSet;
import deepnetts.net.ConvolutionalNetwork;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.opt.OptimizerType;
import deepnetts.util.DeepNettsException;
import deepnetts.util.FileIO;
import visrec.ri.ml.classification.ImageClassifierNetwork;

import javax.visrec.ml.ClassifierCreationException;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.spi.ImageClassifierCreator;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class BufferedImageClassifierCreator implements ImageClassifierCreator<BufferedImage> {

    private static final Logger LOGGER = Logger.getLogger(BufferedImageClassifierCreator.class.getName());

    @Override
    public Class<BufferedImage> getImageClass() {
        return BufferedImage.class;
    }

    @Override
    public ImageClassifier<BufferedImage> create(ImageClassifier.BuildingBlock<BufferedImage> block) throws ClassifierCreationException {
        ImageSet imageSet = new ImageSet(block.getImageWidth(), block.getImageHeight());
        LOGGER.info("Loading images...");

        imageSet.loadLabels(block.getLabelsFile());
        try {
            imageSet.loadImages(block.getTrainingsFile());
            imageSet.shuffle();
        } catch (DeepNettsException | FileNotFoundException ex) {
            throw new ClassifierCreationException("Failed to load images from dataset", ex);
        }

        LOGGER.info("Done!");
        LOGGER.info("Creating neural network...");

        ConvolutionalNetwork neuralNet = null;
        try {
            neuralNet = (ConvolutionalNetwork) FileIO.createFromJson(block.getNetworkArchitecture());
            neuralNet.setOutputLabels(imageSet.getTargetNames());
        } catch (IOException ex) {
            throw new ClassifierCreationException("Failed to create convolutional network from JSON file", ex);
        }

        LOGGER.info("Done!");
        LOGGER.info("Training neural network");

        // create a set of convolutional networks and do training, crossvalidation and performance evaluation
        BackpropagationTrainer trainer = new BackpropagationTrainer(neuralNet)
                .setLearningRate(block.getLearningRate())
                .setMomentum(0.7f)
                .setMaxError(block.getMaxError())
                .setMaxEpochs(block.getMaxEpochs())
                .setBatchMode(false)
                .setOptimizer(OptimizerType.SGD);
        trainer.train(imageSet);

        ImageClassifierNetwork imageClassifier = new ImageClassifierNetwork(neuralNet);
        try {
            FileIO.writeToFile(neuralNet, block.getModelFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new ClassifierCreationException("Failed to write trained model to file", ex);
        }
        return imageClassifier;
    }
}
