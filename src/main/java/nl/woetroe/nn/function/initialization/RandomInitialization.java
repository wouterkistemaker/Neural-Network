package nl.woetroe.nn.function.initialization;

import nl.woetroe.nn.neuron.NeuronConnection;
import nl.woetroe.nn.util.NetworkUtils;

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

public final class RandomInitialization implements InitializationFunction {
    @Override
    public void initialize(Set<NeuronConnection> connections) {
        connections.forEach(c -> c.setWeight(NetworkUtils.nextDouble()));
    }
}
