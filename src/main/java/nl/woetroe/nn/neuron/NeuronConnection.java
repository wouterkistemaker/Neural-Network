package nl.woetroe.nn.neuron;

import nl.woetroe.nn.util.NetworkUtils;

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
