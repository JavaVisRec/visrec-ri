package visrec.ri.spi;

import deepnetts.data.DeepNettsBasicDataSet;
import deepnetts.net.FeedForwardNetwork;
import deepnetts.net.layers.activation.ActivationType;
import deepnetts.net.loss.LossType;
import visrec.ri.ml.classification.FeedForwardNetBinaryClassifier;
import visrec.ri.util.DataSets;

import javax.visrec.ml.ClassifierCreationException;
import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.spi.BinaryClassifierFactory;
import java.io.IOException;

public class FloatArrayBinaryClassifierFactory implements BinaryClassifierFactory<float[]> {

    @Override
    public Class<float[]> getTargetClass() {
        return float[].class;
    }

    @Override
    public BinaryClassifier<float[]> create(NeuralNetBinaryClassifier.BuildingBlock<float[]> block) throws ClassifierCreationException {
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

        DeepNettsBasicDataSet<DeepNettsBasicDataSet.Item> trainingSet = null;
        try {
            trainingSet = DataSets.readCsv(block.getTrainingFile(), block.getInputsNum(), 1, true, ",");
            deepnetts.data.DataSets.normalizeMax(trainingSet);
        } catch (IOException e) {
            throw new ClassifierCreationException("Failed to create training set based on training file", e);
        }
        ffn.train(trainingSet);
        return new FeedForwardNetBinaryClassifier(ffn);
    }
}
