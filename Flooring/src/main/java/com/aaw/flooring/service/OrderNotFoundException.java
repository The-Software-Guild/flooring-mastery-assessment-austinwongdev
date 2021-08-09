/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.service;

/**
 *
 * @author Austin Wong
 */
public class OrderNotFoundException extends Exception{

    public OrderNotFoundException(String message){
        super(message);
    }
    
    public OrderNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}