package nl.woetroe.nn.function.activation;

@Deprecated
public final class ReLuFunction implements ActivationFunction {

    @Override
    public double applyDerivative(double d) {
        return d <= 0 ? 0 : 1;
    }

    @Override
    public Double apply(Double aDouble) {
        return Math.max(0, aDouble);
    }
}
