package javax.visrec.ri.spi;

import deepnetts.data.MLDataItem;
import deepnetts.data.TabularDataSet;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;

import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.model.ModelCreationException;
import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.ri.ml.classification.FeedForwardNetBinaryClassifier;
import javax.visrec.ri.util.DataSets;
import javax.visrec.spi.BinaryClassifierFactory;
import java.io.IOException;

public class FloatArrayBinaryClassifierFactory implements BinaryClassifierFactory<float[]> {

    @Override
    public Class<float[]> getTargetClass() {
        return float[].class;
    }

    @Override
    public BinaryClassifier<float[]> create(NeuralNetBinaryClassifier.BuildingBlock<float[]> block) throws ModelCreationException {
        FeedForwardNetwork.Builder ffnBuilder = FeedForwardNetwork.builder();
        ffnBuilder.addInputLayer(block.getInputsNum());

        for (int h : block.getHiddenLayers()) {
            ffnBuilder.addFullyConnectedLayer(h);
        }

        ffnBuilder.addOutputLayer(1, ActivationType.SIGMOID)
                .lossFunction(LossType.CROSS_ENTROPY);

        FeedForwardNetwork ffn = ffnBuilder.build();
        ffn.getTrainer()
                .setMaxEpochs(block.getMaxEpochs())
                .setMaxError(block.getMaxError())
                .setLearningRate(block.getLearningRate());

        TabularDataSet<MLDataItem> trainingSet = null;
        try {
            trainingSet = DataSets.readCsv(block.getTrainingPath().toFile(), block.getInputsNum(), 1, true, ",");
            //deepnetts.data.DataSets.normalizeMax(trainingSet);
        } catch (IOException e) {
            throw new ModelCreationException("Failed to create training set based on training file", e);
        }
        ffn.train(trainingSet);
        return new FeedForwardNetBinaryClassifier(ffn);
    }
}
