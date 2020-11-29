package nl.woetroe.nn.function.error;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;

public interface ErrorFunction extends ToDoubleBiFunction<Double, Double>, Serializable {

    Double applyDerivative(Double o, Double t);

}
