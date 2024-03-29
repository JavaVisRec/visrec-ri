/**
 * Visual Recognition API for Java, JSR381
 * Copyright (C) 2020  Zoran Sevarac, Frank Greco
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package javax.visrec.ri.spi;

import deepnetts.data.ImageSet;
import deepnetts.net.ConvolutionalNetwork;
import deepnetts.net.train.BackpropagationTrainer;
import deepnetts.net.train.opt.OptimizerType;
import deepnetts.util.DeepNettsException;
import deepnetts.util.FileIO;

import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.ml.model.ModelCreationException;
import javax.visrec.ri.ml.classification.ImageClassifierNetwork;
import javax.visrec.spi.ImageClassifierFactory;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class BufferedImageClassifierFactory implements ImageClassifierFactory<BufferedImage> {

    private static final Logger LOGGER = Logger.getLogger(BufferedImageClassifierFactory.class.getName());

    @Override
    public Class<BufferedImage> getImageClass() {
        return BufferedImage.class;
    }

    @Override
    public ImageClassifier<BufferedImage> create(NeuralNetImageClassifier.BuildingBlock<BufferedImage> block) throws ModelCreationException {
        if (block.getImportPath() != null) {
            return onImport(block);
        }
        return onCreate(block);
    }

    private ImageClassifier<BufferedImage> onImport(NeuralNetImageClassifier.BuildingBlock<BufferedImage> block) throws ModelCreationException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(block.getImportPath().toFile()));
            ConvolutionalNetwork model = (ConvolutionalNetwork) inputStream.readObject();
            return new ImageClassifierNetwork(model);
        } catch (IOException | ClassNotFoundException e) {
            throw new ModelCreationException("Failed to import existing model", e);
        }
    }

    private ImageClassifier<BufferedImage> onCreate(NeuralNetImageClassifier.BuildingBlock<BufferedImage> block) throws ModelCreationException {
        ImageSet imageSet = new ImageSet(block.getImageWidth(), block.getImageHeight());
        LOGGER.info("Loading images...");

        imageSet.loadLabels(block.getLabelsPath().toFile());
        try {
            imageSet.loadImages(block.getTrainingPath().toFile());
            imageSet.shuffle();
        } catch (DeepNettsException | FileNotFoundException ex) {
            throw new ModelCreationException("Failed to load images from dataset", ex);
        }

        LOGGER.info("Done!");
        LOGGER.info("Creating neural network...");

        ConvolutionalNetwork neuralNet = null;
        try {
            neuralNet = (ConvolutionalNetwork) FileIO.createFromJson(block.getNetworkArchitecture().toFile());
            neuralNet.setOutputLabels(imageSet.getTargetColumnsNames());
        } catch (IOException ex) {
            throw new ModelCreationException("Failed to create convolutional network from JSON file", ex);
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
            FileIO.writeToFile(neuralNet, block.getExportPath().toFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new ModelCreationException("Failed to write trained model to file", ex);
        }

        imageClassifier.setThreshold(block.getThreshold());

        return imageClassifier;
    }
}
