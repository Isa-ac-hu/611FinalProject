public class Collateral {
    private String name;
    private double value;

    // Constructor
    public Collateral(String name, double value) {
        this.name = name;
        this.value = value;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for value
    public double getValue() {
        return value;
    }

    // Setter for value
    public void setValue(double value) {
        this.value = value;
    }
}