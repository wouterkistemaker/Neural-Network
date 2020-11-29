package nl.woetroe.nn.neuron;

import nl.woetroe.nn.util.NetworkUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public final class Neuron implements Serializable {

    private static final long serialVersionUID = 4240182149384801865L;
    private double value;
    private final Set<NeuronConnection> connections;
    private boolean isBiasNeuron = false;

    private double delta;

    public Neuron(double value) {
        this.value = value;
        this.connections = new HashSet<>();
    }

    public Neuron() {
        this(NetworkUtils.nextDouble());
    }

    public Neuron(boolean isBiasNeuron) {
        this(isBiasNeuron ? 1 : NetworkUtils.nextDouble());
        this.isBiasNeuron = isBiasNeuron;
    }

    public void connect(Neuron t) {
        this.connections.add(new NeuronConnection(this, t));
    }

    public Set<NeuronConnection> getConnections() {
        return connections;
    }

    public NeuronConnection getConnectionWith(Neuron target) {
        return connections.stream().filter(c -> c.getTo().equals(target) || c.getFrom().equals(target)).findFirst().orElse(null);
    }

    public double getValue() {
        return value;
    }

    public boolean isBiasNeuron() {
        return isBiasNeuron;
    }

    public void adjustValue(double x) {
        value += x;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Neuron{" +
                "value=" + value +
                ", connections=" + connections +
                ", isBiasNeuron=" + isBiasNeuron +
                ", delta=" + delta +
                '}';
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}

