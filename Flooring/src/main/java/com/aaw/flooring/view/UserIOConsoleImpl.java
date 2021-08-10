/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Austin Wong
 */
public class UserIOConsoleImpl implements UserIO {
    
    private final Scanner console = new Scanner(System.in);
     
    /**
     * Takes in a message to display on the console and then waits for an
     * answer from the user to return.
     * @param prompt - String of information to display to the user
     * @return - The answer to the message as a String
     */
    @Override
    public String readString(String prompt) {
        this.print(prompt);
        String answer = console.nextLine();
        this.print("");
        return answer;
    }
    
    /**
     * Takes in a message to display on the console and then waits for the user
     * to input an integer, continually reprompting the user if a non-integer is
     * entered.
     * @param prompt - String of information to display to the user
     * @return - An integer answer to the prompt
     */
    @Override
    public int readInt(String prompt) {
        int num = 0;
        while (true){
            try{
                String stringValue = this.readString(prompt);
                num = Integer.parseInt(stringValue);
                break;
            }
            catch (NumberFormatException ex){
                this.print("Input error. Please try again.\n");
            }
        }
        return num;
    }

    /**
     * Takes in a message to display on the console and continually prompts the
     * user with that message until the user inputs an integer between min and 
     * max (inclusive).
     * @param prompt - String to display to the user
     * @param min - minimum acceptable value for return
     * @param max - maximum acceptable value for return
     * @return - An integer answer to the prompt within range from min to max
     */
    @Override
    public int readInt(String prompt, int min, int max) {
        int num = 0;
        do{
            num = readInt(prompt);
        } while (num < min || num > max);
        return num;
    }

    /**
     * Takes in a message to display on the console and then waits for the user
     * to input a BigDecimal, continually reprompting the user if the input
     * cannot be converted to a BigDecimal.
     * @param prompt - String to display to user
     * @return - BigDecimal answer to the prompt
     */
    @Override
    public BigDecimal readBigDecimal(String prompt) {
        BigDecimal num;
        while (true){
            try{
                String stringValue = this.readString(prompt);
                num = new BigDecimal(stringValue);
                break;
            }
            catch (NumberFormatException ex){
                this.print("Input error. Please try again.\n");
            }
        }
        return num;
    }

    /**
     * Takes in a message to display on the console and continually prompts the
     * user with that message until the user inputs a BigDecimal greater than
     * or equal to min.
     * @param prompt - String to display to user
     * @param min - minimum acceptable value for return
     * @return - BigDecimal response to the prompt greater than or equal to min
     */
    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min) {
        BigDecimal num;
        do{
            num = readBigDecimal(prompt);
        } while (num.compareTo(min) < 0);
        return num;
    }
    
    /**
     * Takes in a message to display on the console and continually prompts the
     * user with that message until the user inputs a BigDecimal greater than
     * or equal to min or the user inputs an empty string
     * @param prompt - String to display to user
     * @param min - minimum acceptable value for return
     * @return - BigDecimal response to the prompt greater than or equal to min
     * or null if empty response
     */
    @Override
    public BigDecimal readBigDecimalOrEmpty(String prompt, BigDecimal min){
        BigDecimal num;
        while (true){
            try{
                String stringValue = this.readString(prompt);
                if (stringValue.isEmpty()){
                    return null;
                }
                num = new BigDecimal(stringValue);
                if (num.compareTo(min) >= 0){
                    break;
                }
            }
            catch (NumberFormatException ex){
                this.print("Input error. Please try again.\n");
            }
        }
        return num;
    }

    /**
     * Takes in a message to display on the console and prompts the user with
     * that message until the user inputs a date in format of MM/dd/yyyy
     * @param prompt - String to display to user
     * @return - LocalDate response to prompt
     */
    @Override
    public LocalDate readDate(String prompt) {
        LocalDate date;
        while (true){
            try{
                String stringValue = this.readString(prompt);
                date = LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                break;
            }
            catch (DateTimeParseException ex){
                this.print("Input error. Please try again in the format: MM/dd/yyyy\n");
            }
        }
        return date;
    }
    
    /**
     * Takes in a message to display on the console and prompts the user with
     * that message until the user inputs a date that is later than min
     * @param prompt - String to display to user
     * @param dayBeforeMin - LocalDate of day before earliest acceptable date
     * @return - LocalDate response to prompt that is later than dayBeforeMin
     */
    @Override
    public LocalDate readDate(String prompt, LocalDate dayBeforeMin) {
        LocalDate date;
        do{
            date = readDate(prompt);
        } while(date.isBefore(dayBeforeMin));
        return date;
    }
    
    /**
     * Takes in a message to display on the console.
     * @param message - String of information to display to the user
     */
    @Override
    public void print(String message) {
        System.out.println(message);
    }
    
    /**
     * Takes in a message to display on the console with a small banner on each
     * side of the message.
     * @param message - String of information to display to the user
     */
    @Override
    public void printWithBanner(String message){
        System.out.println("=== "  + message + " ===");
    }
    
    /**
     * Waits for user to press Enter before continuing program
     */
    @Override
    public void pressEnterToContinue(){
        this.readString("Press Enter to continue.");
        this.print("");
    }

    /**
     * Takes in a message to display on the console and prompts the user with
     * that message until the user responds with case-sensitive Y or N
     * @param prompt - String to display to user
     * @return - Boolean of response to prompt
     */
    @Override
    public boolean readYesOrNo(String prompt){
        String answer;
        do{
            answer = readString(prompt);
        } while(!answer.equals("Y") && !answer.equals("N"));
        
        return answer.equals("Y");
    }
}
