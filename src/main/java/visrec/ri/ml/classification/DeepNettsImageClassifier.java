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

    public DeepNettsImageClassifier(ConvolutionalNetwork network) {
        super(BufferedImage.class, network);
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

}
