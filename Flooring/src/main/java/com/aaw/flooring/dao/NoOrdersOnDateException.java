/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Austin Wong
 */
public class NoOrdersOnDateException extends Exception{

    public NoOrdersOnDateException(String message){
        super(message);
    }
    
    public NoOrdersOnDateException(String message, Throwable cause){
        super(message, cause);
    }
}