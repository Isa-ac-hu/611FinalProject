import java.time.LocalDate;

public class Loan implements Observer{

    private double initialLoanAmount;
    private double debtAmount;
    private Collateral collateral;
    private LocalDate loanStartDate;
    private double annualInterestRate;

    private Customer customer;

    public Loan(double initialLoanAmount, Collateral collateral, Customer customer){
        this.initialLoanAmount = initialLoanAmount;
        this.collateral = collateral;
        debtAmount = initialLoanAmount;
        annualInterestRate = 0.5;
        loanStartDate = Bank.getTime();
        this.customer = customer;
    }

    public void update() {
        // Calculate the number of days elapsed since the last update
        LocalDate currentDate = Bank.getTime(); // Assuming Bank has a method to get the current time
        long monthsElapsed = loanStartDate.until(currentDate).getMonths();

        // Calculate the interest amount to add
        double monthlyInterest = annualInterestRate / 12; // Convert annual rate to daily rate
        double interestToAdd = debtAmount;
        for(int i = 0; i < monthsElapsed; i++){
            interestToAdd *= monthlyInterest;
        }
        debtAmount += interestToAdd;
        loanStartDate = currentDate;
    }
}
