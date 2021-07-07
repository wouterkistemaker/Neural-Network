package nl.wouterkistemaker.neuralnetwork.util;

public interface ChangeListener<T> {

    void handle(T oldValue, T newValue);
}