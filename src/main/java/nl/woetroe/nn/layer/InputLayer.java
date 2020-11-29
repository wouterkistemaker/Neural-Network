package nl.woetroe.nn.layer;

import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.function.error.ErrorFunction;
import nl.woetroe.nn.neuron.Neuron;

import java.util.Arrays;

public final class InputLayer extends Layer {

    private static final long serialVersionUID = -6512736351539200069L;

    public InputLayer(int size, boolean withBias, ActivationFunction activationFunction, ErrorFunction errorFunction) {
        super(size, withBias, activationFunction, errorFunction);
    }

    public InputLayer(int size, boolean withBias){
        super(size, withBias);
    }

    public InputLayer(int size){
        super(size);
    }

    public void setInput(double... input) {
        Arrays.stream(input).forEach(i -> this.getNeurons().add(new Neuron(i)));
    }


}
