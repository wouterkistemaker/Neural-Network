package nl.wouterkistemaker.neuralnetwork.function.initialization;

import nl.wouterkistemaker.neuralnetwork.util.NetworkUtils;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.neuron.NeuronConnection;

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
public final class XavierInitialization implements InitializationFunction {

    private static final long serialVersionUID = -3562790173733472660L;

    @Override
    public void initialize(Layer previous, NeuronConnection connection) {
        final int nInputNodes = previous.getSize();

        // weight = U [-(1/sqrt(n)), 1/sqrt(n)]
        final double bound = 1 / Math.sqrt(nInputNodes);
        connection.setWeight(NetworkUtils.nextDouble(-bound, bound));
    }
}
