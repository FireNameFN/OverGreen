package overgreen;

final class OverGreenMath {
    public static double expDecay(double a, double b, double decay, double delta) {
        return b + (a - b) * Math.exp(-decay * delta);
    }
}
