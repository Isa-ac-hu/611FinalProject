public class UsdToJpyConversion implements CurrencyConversionStrategy {
    private static final double EXCHANGE_RATE = 114.50; // Example rate

    @Override
    public double convertTo(double usdAmount) {
        return usdAmount * EXCHANGE_RATE;
    }

    @Override
    public double convertBack(double amount) {
        return amount * EXCHANGE_RATE;
    }
}