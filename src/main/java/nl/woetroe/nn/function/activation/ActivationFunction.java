package nl.woetroe.nn.function.activation;

import java.io.Serializable;
import java.util.function.Function;

public interface ActivationFunction extends Function<Double, Double>, Serializable {

    double applyDerivative(double d);


}

