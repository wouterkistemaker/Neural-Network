package nl.woetroe.nn.layer;

import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.function.error.ErrorFunction;
import nl.woetroe.nn.neuron.Neuron;

public final class DenseLayer extends Layer {

    private static final long serialVersionUID = -4158063030455588345L;

    public DenseLayer(int size, boolean withBias, ActivationFunction activationFunction, ErrorFunction errorFunction) {
        super(size, withBias, activationFunction, errorFunction);
    }

    public DenseLayer(int size, boolean withBias){
        super(size, withBias);
    }


    public DenseLayer(int size) {
        super(size);
    }

    public void fillFixed(double value) {
        for (int i = 0; i < getSize(); i++) {
            getNeurons().add(new Neuron(value));
        }
    }

    public void fillRandom() {
        for (int i = 0; i < getSize(); i++) {
            getNeurons().add(new Neuron());

        }
    }
}
