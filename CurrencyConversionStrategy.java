public interface CurrencyConversionStrategy {
    double convertTo(double amount);

    double convertBack(double amount);
}
