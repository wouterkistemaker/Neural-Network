package nl.woetroe.nn.neuron;

import java.io.Serializable;

/*
 * Copyright (C) 2020-2021, Wouter Kistemaker.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * Represents the connection between two neurons. This connection can be
 * compared to the human brain's axons. A human brain expresses the value
 * of certain neurons in brain activity, whereas this connection is given
 * a weight that signals the importance of this connection
 */
public final class NeuronConnection implements Serializable {

    private static final long serialVersionUID = 5132579330613761051L;
    private final Neuron from;
    private final Neuron to;
    private double weight;

    /**
     * Constructs a new {@link NeuronConnection}
     *
     * @param from   first neuron
     * @param to     second neuron
     * @param weight weight of the connection (importance)
     */
    public NeuronConnection(Neuron from, Neuron to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Constructs a new {@link NeuronConnection}
     *
     * @param from first neuron
     * @param to   second neuron
     */
    public NeuronConnection(Neuron from, Neuron to) {
        this.from = from;
        this.to = to;
        this.weight = 0; // was random before
    }

    /**
     * @return the second {@link Neuron}
     */
    public Neuron getTo() {
        return to;
    }

    /**
     * @return the first {@link Neuron}
     */
    public Neuron getFrom() {
        return from;
    }

    /**
     * @return the weight of this connection, which signals the
     * importance of this {@link NeuronConnection}
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Helper-functions that changes the current weight
     * of this {@link NeuronConnection}
     *
     * @param x adjustment of the weight
     */
    public void adjustWeight(double x) {
        this.weight += x;
    }

    /**
     * Sets the weight of this connection
     *
     * @param x the weight of this connection
     */
    public void setWeight(double x) {
        this.weight = x;
    }
}
