package nl.wouterkistemaker.neuralnetwork.util.observable;

import java.util.Collection;

public interface Observable<T> {

    void setValue(T value);

    T getValue();

    Collection<ChangeListener<T>> getListeners();

    default void addListener(ChangeListener<T> listener) {
        this.getListeners().add(listener);
    }

    default void removeListener(ChangeListener<T> listener) {
        this.getListeners().remove(listener);
    }
}
