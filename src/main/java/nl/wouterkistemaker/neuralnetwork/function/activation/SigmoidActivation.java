package nl.wouterkistemaker.neuralnetwork.function.activation;

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
public final class SigmoidActivation implements ActivationFunction {
    private static final long serialVersionUID = 2684630499328743102L;

    @Override
    public double activate(double wSum) {
        return ((1 / (1 + Math.exp(-wSum))));
    }
}