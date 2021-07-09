package nl.wouterkistemaker.neuralnetwork.neuron;

/*
  Copyright (C) 2020-2021, Wouter Kistemaker.
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published
  by the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.
  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import nl.wouterkistemaker.neuralnetwork.util.NetworkUtils;
import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.exception.NoSuchConnectionException;
import nl.wouterkistemaker.neuralnetwork.function.error.CostFunction;

import java.io.Serializable;
import java.util.*;

/**
 * Represents an artificial neuron, vaguely related to the biological neuron
 * found in a human brain
 */
public class Neuron implements Serializable {

    private static final long serialVersionUID = 6952458116227426483L;
    private final UUID id;
    private double value;
    private double delta;
    private double error;
    private final Set<NeuronConnection> connections;

    /**
     * Constructs a new neuron with a give value
     *
     * @param value initial value of the neuron
     */
    public Neuron(double value) {
        this.id = UUID.randomUUID();
        this.value = value;
        this.connections = new HashSet<>();
    }

    /**
     * Constructs a new neuron with a fixed or random value between 0 and 1
     *
     * @param random whether or not to randomly initialize this neuron
     *               {@code true}neuron value is random between 0 and 1
     *               {@code false}neuron value is 0.5
     */
    public Neuron(boolean random) {
        this(!random ? 0.5 : NetworkUtils.nextDouble());
    }

    /**
     * Constructs a new neuron with a random value between 0 and 1
     */
    public Neuron() {
        this(true);
    }

    /**
     * Constructs a new {@link NeuronConnection} between this neuron and the target neuron
     *
     * @param target the receiving end of the newly constructed {@link NeuronConnection}
     */
    public void connect(Neuron target) {
        connections.add(new NeuronConnection(this, target));
    }

    /**
     * Getter function that retrieves the {@link NeuronConnection} where this neuron is the starting
     * point and the neuron passed as a parameter is the receiving end.
     *
     * @param other receiving end of the existing {@link NeuronConnection}
     * @return the {@link NeuronConnection} between this neuron and the other neuron.
     * @implNote If no connection was found starting from this neuron, a warning is given in the console.
     * Then, a {@link NeuronConnection} is searched where this neuron is the receiving end and not
     * the starting point.
     */
    public NeuronConnection getConnectionWith(Neuron other) {
        Optional<NeuronConnection> optional = connections.stream().filter(c -> c.getTo().equals(other)).findFirst();
        if (optional.isEmpty()) {
            System.err.println("Potential algorithm break (!)");

            final RuntimeException exception = new RuntimeException();
            System.out.println(exception.getStackTrace()[1]);

            return connections.stream().filter(c -> c.getFrom().equals(other)).findFirst().orElseThrow(NoSuchConnectionException::new);
        }

        return optional.get();
    }

    /**
     * @return a {@link LinkedHashSet} of {@link NeuronConnection NeuronConnections}
     */
    public Set<NeuronConnection> getConnections() {
        return connections;
    }

    /**
     * @return whether or not this neuron is connected to the other neuron
     */
    public boolean isConnectedTo(Neuron other) {
        return connections.stream().anyMatch(c -> c.getTo().equals(other));
    }

    /**
     * @return the value of this neuron
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of this neuron
     *
     * @param value the new value of this neuron
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns the error of this neuron, which is the output of the
     * {@link CostFunction} applied to this neuron's value and
     * the target output of this neuron
     *
     * @return the error
     */
    public double getError() {
        return error;
    }

    /**
     * Sets the error of this neuron, which is the output of the
     * {@link CostFunction} applied to this neuron's value and
     * the target output of this neuron
     *
     * @deprecated because this is a very important function
     * that may fail the whole {@link NeuralNetwork} and its
     * training process if used incorrectly, therefore caution is required,
     * hence is it has been deprecated
     */
    @Deprecated
    public void setError(double error) {
        this.error = error;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neuron neuron = (Neuron) o;
        return Double.compare(neuron.value, value) == 0 && Objects.equals(id, neuron.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, id);
    }


}
