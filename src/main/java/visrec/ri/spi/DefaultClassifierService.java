package visrec.ri.spi;

import deepnetts.data.ImageSet;
import deepnetts.net.ConvolutionalNetwork;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.opt.OptimizerType;
import deepnetts.util.DeepNettsException;
import deepnetts.util.FileIO;
import visrec.ri.ml.classification.ImageClassifierNetwork;

import javax.visrec.ml.ClassifierCreationException;
import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.spi.ClassifierService;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author Kevin Berendsen
 */
public final class DefaultClassifierService implements ClassifierService {

    private static final Logger LOGGER = Logger.getLogger(DefaultClassifierService.class.getName());

    @Override
    public <T> ImageClassifier<T> createImageClassifier(ImageClassifier.BuildingBlock<T> block) throws ClassifierCreationException {
        if(!block.getImageClass().equals(BufferedImage.class)) {
            throw new ClassifierCreationException("Only BufferedImage is supported as image type");
        }
        ImageSet imageSet = new ImageSet(block.getImageWidth(), block.getImageHeight());
        LOGGER.info("Loading images...");

        imageSet.loadLabels(block.getLabelsFile());
        try {
            imageSet.loadImages(block.getTrainingsFile());
            imageSet.shuffle();
        } catch (DeepNettsException | FileNotFoundException ex) {
            throw new ClassifierCreationException("Unable to load images from dataset", ex);
        }

        LOGGER.info("Done!");
        LOGGER.info("Creating neural network...");

        ConvolutionalNetwork neuralNet = null;
        try {
            neuralNet = (ConvolutionalNetwork) FileIO.createFromJson(block.getNetworkArchitecture());
            neuralNet.setOutputLabels(imageSet.getTargetNames());
        } catch (IOException ex) {
            throw new ClassifierCreationException("Unable to create convolutional network from JSON file", ex);
        }

        LOGGER.info("Done!");
        LOGGER.info("Training neural network");

        // create a set of convolutional networks and do training, crossvalidation and performance evaluation
        BackpropagationTrainer trainer = new BackpropagationTrainer(neuralNet)
                .setLearningRate(block.getLearningRate())
                .setMaxEpochs(block.getMaxEpochs())
                .setMomentum(0.7f)
                .setMaxError(block.getMaxError())
                .setBatchMode(false)
                .setOptimizer(OptimizerType.SGD);
        trainer.train(imageSet);

        ImageClassifierNetwork imageClassifier = new ImageClassifierNetwork(neuralNet);
        try {
            FileIO.writeToFile(neuralNet, block.getModelFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new ClassifierCreationException("Unable to write trained model to file", ex);
        }
            return (ImageClassifier<T>) imageClassifier;
    }

    @Override
    public BinaryClassifier createBinaryClassifier(BinaryClassifier.BuildingBlock block) throws ClassifierCreationException {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
