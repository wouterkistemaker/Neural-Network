import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.neuron.BiasNeuron;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

public class ObjectTests {
    @Test
    public void testConnections() {
        final Neuron neuron = new Neuron(0.1);
        final Neuron bias = new BiasNeuron();

        Assertions.assertFalse(neuron.isConnectedTo(bias));
        Assertions.assertFalse(bias.isConnectedTo(neuron));

        neuron.connect(bias);

        Assertions.assertTrue(neuron.isConnectedTo(bias));
        Assertions.assertFalse(bias.isConnectedTo(neuron));
    }

    @Test
    public void testLayers() {
        final Layer first = new Layer(2);
        final Layer last = new Layer(1);

        final Neuron lastNeuron = (Neuron) last.getNeurons().toArray()[0];

        Assertions.assertTrue(first.getNeurons().stream().noneMatch(n -> n.isConnectedTo(lastNeuron)));
        first.connect(last);
        Assertions.assertFalse(first.getNeurons().stream().noneMatch(n -> n.isConnectedTo(lastNeuron)));
        Assertions.assertTrue(first.getNeurons().stream().noneMatch(lastNeuron::isConnectedTo));
    }
}
