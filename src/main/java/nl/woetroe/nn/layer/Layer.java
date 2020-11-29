package nl.woetroe.nn.layer;

import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.function.activation.SigmoidFunction;
import nl.woetroe.nn.function.error.ErrorFunction;
import nl.woetroe.nn.function.error.MeanSquaredFunction;
import nl.woetroe.nn.neuron.Neuron;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class Layer implements Serializable {

    private static final ActivationFunction DEFAULT_ACTIVATION_FUNCTION = new SigmoidFunction();
    private static final ErrorFunction DEFAULT_ERROR_FUNCTION = new MeanSquaredFunction();

    private static final Neuron BIAS = new Neuron(true);
    private static final long serialVersionUID = -7022757345368501174L;

    private final int size;
    private final boolean withBias;
    private final List<Neuron> neurons;

    // functions
    private final ActivationFunction activationFunction;
    private final ErrorFunction errorFunction;

    public Layer(int size, boolean withBias, ActivationFunction activationFunction, ErrorFunction errorFunction) {
        this.size = size;
        this.withBias = withBias;
        this.neurons = new LinkedList<>();
        this.activationFunction = activationFunction == null ? DEFAULT_ACTIVATION_FUNCTION : activationFunction;
        this.errorFunction = errorFunction == null ? DEFAULT_ERROR_FUNCTION : errorFunction;
    }

    public Layer(int size, boolean withBias) {
        this(size, withBias, null, null);
    }

    public Layer(int size) {
        this(size, false);
    }

    public void connectToLayer(Layer target) {
        for (Neuron t : target.getNeurons()) {
            for (Neuron n : neurons) {
                n.connect(t);
            }

            if (hasBias()) {
                getBias().connect(t);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public boolean hasBias() {
        return withBias;
    }

    public Neuron getBias() {
        return BIAS;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public ErrorFunction getErrorFunction() {
        return errorFunction;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "size=" + size +
                ", withBias=" + withBias +
                (withBias ? (", bias=" + BIAS.toString()) : "") +
                ", neurons=" + neurons +
                ", activationFunction=" + activationFunction +
                '}';
    }
}
