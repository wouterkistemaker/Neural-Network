package nl.woetroe.nn.function.error;

public class MeanSquaredFunction implements ErrorFunction {

    private static final long serialVersionUID = -7035473701055990378L;

    @Override
    public double applyAsDouble(Double t, Double y) {
        return 0.5 * Math.pow((t - y), 2);
    }

    @Override
    public Double applyDerivative(Double o, Double t) {
        return Math.abs(t-o);
    }
}
