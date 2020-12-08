package nl.woetroe.nn.data;

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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DataSet<T> {

    private final int size;
    private final List<T> data;

    public DataSet(int size) {
        this.size = size;
        this.data = new LinkedList<>();
    }

    public DataSet(List<T> set) {
        this.size = (this.data = set).size();
    }

    public final int getSize() {
        return size;
    }

    public final List<T> getData() {
        return data;
    }

    public final void add(T value) {
        checkSize();
        data.add(value);
    }

    public final void add(T... values) {
        Arrays.stream(values).forEach(this::add);
    }

    private void checkSize() {
        if (data.size() > size) {
            throw new IllegalStateException();
        }
    }

}
