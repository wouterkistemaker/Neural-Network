package nl.wouterkistemaker.neuralnetwork.util.observable;

public interface ChangeListener<T> {

    void handle(T oldValue, T newValue);
}