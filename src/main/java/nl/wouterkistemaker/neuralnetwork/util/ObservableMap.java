package nl.wouterkistemaker.neuralnetwork.util;

import java.util.*;

public final class ObservableMap<K, V> extends HashMap<K, V> implements Observable<Map<? extends K, ? extends V>> {

    private Map<? extends K, ? extends V> value;
    private final Set<ChangeListener<Map<? extends K, ? extends V>>> listeners;

    @SafeVarargs
    public ObservableMap(Map<? extends K, ? extends V> value, ChangeListener<Map<? extends K, ? extends V>>... listeners) {
        super(value);

        this.value = value;
        this.listeners = new LinkedHashSet<>(Arrays.asList(listeners));
    }

    @Deprecated
    @Override
    public void setValue(Map<? extends K, ? extends V> value) {
        Map<? extends K, ? extends V> oldValue = this.value;

        this.value = value;
        this.emit(oldValue,value);
    }

    @Deprecated
    @Override
    public Map<? extends K, ? extends V> getValue() {
        return value;
    }

    @Override
    public Collection<ChangeListener<Map<? extends K, ? extends V>>> getListeners() {
        return listeners;
    }

    private void emit(Map<? extends K, ? extends V> oldValue, Map<? extends K, ? extends V> value) {
        this.listeners.forEach(l -> l.handle(oldValue, value));
    }
}
