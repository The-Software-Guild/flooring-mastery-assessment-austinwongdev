/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 8, 2021
 * purpose: 
 */

package com.aaw.flooring.view;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Austin Wong
 */
public interface UserIO {

    String readString(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
    BigDecimal readBigDecimal(String prompt);
    BigDecimal readBigDecimal(String prompt, BigDecimal min);
    BigDecimal readBigDecimalOrEmpty(String prompt, BigDecimal min);
    LocalDate readDate(String prompt);
    LocalDate readDate(String prompt, LocalDate dayBeforeMin);
    void print(String message);
    void printWithBanner(String message);
    void pressEnterToContinue();
    boolean readYesOrNo(String prompt);
}
