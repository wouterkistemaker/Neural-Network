package nl.woetroe.nn.function.activation;

public final class SigmoidFunction implements ActivationFunction {
    private static final long serialVersionUID = -4569160516529868553L;

    @Override
    public Double apply(Double x) {
        return 1 / (1 + (Math.exp(-x)));
    }

    @Override
    public double applyDerivative(double d) {
        return (apply(d) * (1 - apply(d)));
    }

    @Override
    public String toString() {
        return "Sigmoid Function";
    }

}
