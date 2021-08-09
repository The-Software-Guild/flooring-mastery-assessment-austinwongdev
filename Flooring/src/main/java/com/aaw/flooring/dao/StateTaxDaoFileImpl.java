/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import com.aaw.flooring.model.StateTax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Austin Wong
 */
public class StateTaxDaoFileImpl implements StateTaxDao {
    
    private final String STATE_TAX_FILE;
    private final String DELIMITER = "::";
    private final Map<String, StateTax> stateTaxMap = new HashMap<>();
    
    public StateTaxDaoFileImpl(){
        this.STATE_TAX_FILE = "src/main/resources/Data/Taxes.txt";
    }
    
    public StateTaxDaoFileImpl(String stateTaxFilePath){
        this.STATE_TAX_FILE = stateTaxFilePath;
    }
    
    @Override
    public StateTax getStateTax(String stateAbbreviation){
        return stateTaxMap.get(stateAbbreviation);
    }
    
    @Override
    public StateTax addStateTax(StateTax stateTax){
        return stateTaxMap.put(stateTax.getStateAbbreviation(), stateTax);
    }
    
    /**
     * Loads state taxes from each line of file. Each line is assumed to be a
     * separate state tax and a header line is assumed.
     * @throws OrderPersistenceException 
     */
    @Override
    public void loadStateTaxes() throws OrderPersistenceException{
        try{
            Scanner scan = new Scanner(new BufferedReader(new FileReader(STATE_TAX_FILE)));
            
            // Skip header
            if (scan.hasNextLine()){
                scan.nextLine();
            }
            
            // Parse file contents
            while (scan.hasNextLine()){
                StateTax nextStateTax = unmarshallStateTax(scan.nextLine());
                addStateTax(nextStateTax);
            }
            
            scan.close();
        } catch (FileNotFoundException ex){
            throw new OrderPersistenceException("Could not load state taxes", ex);
        }
    }
    
    /**
     * Takes in a String and converts it to a StateTax object.
     * @param stateTaxAsText - The fields of a StateTax object in String form.
     * @return - StateTax object
     */
    private StateTax unmarshallStateTax(String stateTaxAsText){
        String[] stateTaxFields = stateTaxAsText.split(DELIMITER);
        String stateAbbreviation = stateTaxFields[0];
        String stateName = stateTaxFields[1];
        BigDecimal taxRate = new BigDecimal(stateTaxFields[2]);
        StateTax newStateTax = new StateTax(stateAbbreviation, stateName,
                                         taxRate);
        return newStateTax;
    }
    
}
