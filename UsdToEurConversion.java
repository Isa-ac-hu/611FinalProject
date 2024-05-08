public class UsdToEurConversion implements CurrencyConversionStrategy {
    private static final double EXCHANGE_RATE = 0.92; // Example rate, should be updated with real-time data if possible

    @Override
    public double convertTo(double usdAmount) {
        return usdAmount * EXCHANGE_RATE;
    }

    @Override
    public double convertBack(double amount) {
        return amount * EXCHANGE_RATE;
    }
}