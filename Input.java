import java.util.List;
import java.util.Objects;
import java.util.Scanner;
/**
 * Handles user input from the console for various types of data, including integers and strings, with
 * validations for input range and length. It utilizes the {@code Scanner} class to read user input and
 * provides methods to ensure that this input meets specific criteria.
 *
 *
 */
public class Input {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getIntInput(int lowerBound, int upperBound, String prompt){
        System.out.println(prompt);
        Integer choice = 0;
        boolean cond = true;
        while (cond){
            if (scanner.hasNextInt()){
                choice = scanner.nextInt();
                if (choice < lowerBound || choice > upperBound) {
                    System.out.println("Invalid number. Please enter a number between " + lowerBound + " and " + upperBound + ".");
                }
                else{
                    cond = false;
                }
            }
            else{
                System.out.println("Input is invalid. Please enter a number:");
            }
        }
        return choice;
    }

    public static int getIntInput(List<Integer> numSet, String prompt){
        int choice = 0;
        boolean cond = true;
        while (cond){
            if (scanner.hasNextInt()){
                choice = scanner.nextInt();
                if (!numSet.contains(choice) ) {
                    System.out.println("Invalid number. Please enter a number from the following: " + numSet + ".");
                }
                else{
                    cond = false;
                }
            }
            else{
                    System.out.println("Input is invalid. Please enter a number:");
            }

        }
        return choice;
    }

    public static String getStringInput(int inputLen, String prompt){
        System.out.println(prompt);
        String input = "";
        boolean condition = false;
        while (!condition) {
            input = scanner.next();
            if (input.length() > inputLen){
                System.out.println("Error - please shorten your input to be less than " + inputLen + "characters long.");
            }
            else if (input.matches(".*[0-9].*")){
                System.out.println("Error - input cannot include a number, please retry.");
            }
            else{
                condition = true;
            }
        }
        return input;
    }



    public static String getStringInput(int inputLen, String prompt, List<String> validInputs) {
        System.out.println(prompt);
        String input = "";
        boolean condition = false;
        while (!condition) {
            input = scanner.next();

            if (input.length() > inputLen) {
                System.out.println("Error - please shorten your input to be less than " + inputLen + " characters long.");
            } else if (input.matches(".*[0-9].*")) {
                System.out.println("Error - input cannot include a number, please retry.");
            } else {
                System.out.println("Error - input is not a valid option. Please enter one of the following: " + String.join(", ", validInputs));
            }
        }
        return input;
    }




}