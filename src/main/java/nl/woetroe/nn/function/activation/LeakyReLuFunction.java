package nl.woetroe.nn.function.activation;

@Deprecated
public final class LeakyReLuFunction implements ActivationFunction {

    // likely to be chosen relatively small
    private final double constant;

    public LeakyReLuFunction(double constant) {
        this.constant = constant;
    }

    @Override
    public double applyDerivative(double d) {
        if (d > 0) return 1;
        return constant;
    }

    @Override
    public Double apply(Double aDouble) {
        if (aDouble > 0) return aDouble;
        return constant * aDouble;
    }
}
