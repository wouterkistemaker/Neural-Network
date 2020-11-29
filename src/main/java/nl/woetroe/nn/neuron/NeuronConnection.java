package nl.woetroe.nn.neuron;

import nl.woetroe.nn.util.NetworkUtils;

import java.io.Serializable;

public final class NeuronConnection implements Serializable {

    private static final long serialVersionUID = 5132579330613761051L;
    private final Neuron from;
    private final Neuron to;
    private double weight;

    public NeuronConnection(Neuron from, Neuron to) {
        this.from = from;
        this.to = to;
        this.weight = NetworkUtils.nextDouble();
    }

    public Neuron getTo() {
        return to;
    }

    public Neuron getFrom() {
        return from;
    }

    public double getWeight() {
        return weight;
    }

    public void adjustWeight(double x) {
        this.weight += x;
    }
}
