package nl.wouterkistemaker.neuralnetwork;

import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.neuron.NeuronConnection;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkFrame;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkPanel;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

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
public final class NeuralNetwork {

    private final Set<Layer> layers;

    public NeuralNetwork(Layer... layers) {
        this.layers = new LinkedHashSet<>(Arrays.asList(layers));
        this.connect();
    }

    /**
     * Function that initiates the process of connecting each individual
     * {@link Layer} to the next {@link Layer} in the network
     */
    private void connect() {
        final Layer[] layerArray = layers.toArray(new Layer[0]);
        for (int i = 0; i < layerArray.length; i++) {
            if ((i + 1) >= layerArray.length) break;

            Layer current = layerArray[i];
            Layer next = layerArray[i + 1];

            current.connect(next);
        }
    }

    /**
     * Helper function that creates a comprehensible overview of this network's architecture
     */
    public final void visualize() {
        new NeuralNetworkFrame(new NeuralNetworkPanel(layers.toArray(new Layer[0])));
    }

    /**
     * @return a {@link LinkedHashSet} of {@link Layer Layers}
     */
    public Set<Layer> getLayers() {
        return layers;
    }
}
