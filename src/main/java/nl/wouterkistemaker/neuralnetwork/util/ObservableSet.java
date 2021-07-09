package nl.wouterkistemaker.neuralnetwork.util;

import java.util.*;

public final class ObservableSet<T> extends HashSet<T> implements Observable<Collection<? extends T>> {

    private Collection<? extends T> value;
    private final Set<ChangeListener<Collection<? extends T>>> listeners;

    @SafeVarargs
    public ObservableSet(Collection<? extends T> value, ChangeListener<Collection<? extends T>>... listeners) {
        super(value);

        this.value = value;
        this.listeners = new LinkedHashSet<>(Arrays.asList(listeners));
    }

    public ObservableSet() {
        this(new HashSet<>());
    }

    @Deprecated
    @Override
    public void setValue(Collection<? extends T> value) {
        Collection<? extends T> oldValue = this.value;
        this.value = value;

        listeners.forEach(l -> l.handle(oldValue, value));
    }

    @Deprecated
    @Override
    public Collection<? extends T> getValue() {
        return value;
    }

    @Override
    public Collection<ChangeListener<Collection<? extends T>>> getListeners() {
        return listeners;
    }
}
