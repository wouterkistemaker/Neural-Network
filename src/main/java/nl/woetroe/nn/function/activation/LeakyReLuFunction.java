package nl.woetroe.nn.function.activation;

/**
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
@Deprecated
public final class LeakyReLuFunction implements ActivationFunction {

    // likely to be chosen relatively small
    private final double constant;

    public LeakyReLuFunction(double constant) {
        this.constant = constant;
    }

    @Override
    public double applyDerivative(double d) {
        return (d > 0 ? 1 : constant);
    }

    @Override
    public Double apply(Double d) {
        return (d > 0) ? d:(constant * d);
    }
}
