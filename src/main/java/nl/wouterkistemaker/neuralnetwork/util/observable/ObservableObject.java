package nl.wouterkistemaker.neuralnetwork.util.observable;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ObservableObject<T> implements Observable<T> {

    private T value;
    private final Set<ChangeListener<T>> listeners;

    @SafeVarargs
    public ObservableObject(T value, ChangeListener<T>... listeners) {
        this.value = value;
        this.listeners = new LinkedHashSet<>(Arrays.asList(listeners));
    }

    @Override
    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        this.emit(oldValue, value);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public Collection<ChangeListener<T>> getListeners() {
        return listeners;
    }

    private void emit(T oldValue, T value) {
        listeners.forEach(l -> l.handle(oldValue, value));
    }
}
