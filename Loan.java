public class Loan implements Observer {

    private double initialLoanAmount;
    private double debtAmount;
    private Collateral collateral;
    private int loanStartDate;
    private double annualInterestRate;
    private int customerID;
    static int currentLoanID = 0;
    private int loanID;

    public Loan(int loanID, int customerID, double initialLoanAmount, double debtAmount, Collateral collateral, int loanStartDate){
        this.initialLoanAmount = initialLoanAmount;
        this.collateral = collateral;
        this.debtAmount = initialLoanAmount;
        this.annualInterestRate = 0.5;
        this.loanStartDate = loanStartDate;
        this.customerID = customerID;
        this.loanID = currentLoanID;
        currentLoanID++;
    }

    public void update() {
        // Calculate the number of days elapsed since the last update
        int currentDate = Bank.getTime(); // Assuming logic.Bank has a method to get the current time
        long monthsElapsed = currentDate - loanStartDate;

        // Calculate the interest amount to add
        double monthlyInterest = annualInterestRate / 12; // Convert annual rate to daily rate
        double interestToAdd = debtAmount;
        for(int i = 0; i < monthsElapsed; i++){
            interestToAdd *= monthlyInterest;
        }
        debtAmount += interestToAdd;
        loanStartDate = currentDate;
    }

    public int getCustomerID(){
        return customerID;
    }
    public double getInitialLoanAmount() {
        return initialLoanAmount;
    }

    public void setInitialLoanAmount(double initialLoanAmount) {
        this.initialLoanAmount = initialLoanAmount;
    }

    public double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(double debtAmount) {
        this.debtAmount = debtAmount;
    }

    public Collateral getCollateral() {
        return collateral;
    }

    public void setCollateral(Collateral collateral) {
        this.collateral = collateral;
    }

    public int getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(int loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }


    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public static int getCurrentLoanID(){
        return currentLoanID;
    }

    public String toString(){
        return "LoanID: " + loanID + " Amount Owed: " + debtAmount + " Creation Date: " + loanStartDate + " Collateral: " + collateral.getName();
    }
}
