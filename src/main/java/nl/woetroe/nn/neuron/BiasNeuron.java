package nl.woetroe.nn.neuron;

import nl.woetroe.nn.util.NetworkUtils;

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
public final class BiasNeuron extends Neuron {

    private double weight;

    public BiasNeuron() {
        super(1);
        this.weight = NetworkUtils.nextDouble();
    }

    public BiasNeuron(double weight) {
        super(1);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void adjustWeight(double x) {
        this.weight += x;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
