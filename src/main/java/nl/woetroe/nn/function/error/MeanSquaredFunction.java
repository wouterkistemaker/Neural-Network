package nl.woetroe.nn.function.error;

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
public class MeanSquaredFunction implements ErrorFunction {

    private static final long serialVersionUID = -7035473701055990378L;

    @Override
    public double applyAsDouble(Double t, Double y) {
        return 0.5 * Math.pow((t - y), 2);
    }

    @Override
    public Double applyDerivative(Double o, Double t) {
        return Math.abs(t - o);
    }
}
