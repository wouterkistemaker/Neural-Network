package nl.wouterkistemaker.neuralnetwork.function.transfer;

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
public final class LeakyReLUTransfer implements TransferFunction {

    @Override
    public double apply(double wSum) {
        if (wSum > 0) return wSum;
        return 0.01 * wSum;
    }

    @Override
    public double unapply(double val) {
        if (val > 0) return val;
        return val * 100;
    }

    @Override
    public double applyDerivative(double wSum) {
        return (wSum > 0) ? 1 : 0.01;
    }
}
