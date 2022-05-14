import nl.wouterkistemaker.neuralnetwork.util.observable.ChangeListener;
import nl.wouterkistemaker.neuralnetwork.util.observable.Observable;

import java.util.Collection;
import java.util.HashSet;

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
public class ObservableTests {

    public static void main(String[] args) {
        GraphicTestObject obj = new GraphicTestObject(2.43);
        obj.addListener((oldValue, newValue) -> System.out.printf("Obj changed from %s to %s", oldValue, newValue));

        obj.setValue(0.1);
    }

    private static final class GraphicTestObject implements Observable<Double> {

        private double value;
        private final Collection<ChangeListener<Double>> listeners = new HashSet<>();

        public GraphicTestObject(double value) {
            this.value = value;
        }

        @Override
        public void setValue(Double value) {
            Double old = this.value;
            this.value = value;

            listeners.forEach(l-> l.handle(old, value));
        }

        @Override
        public Double getValue() {
            return value;
        }

        @Override
        public Collection<ChangeListener<Double>> getListeners() {
            return listeners;
        }
    }

}
