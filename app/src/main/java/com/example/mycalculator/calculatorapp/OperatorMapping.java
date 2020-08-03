 package com.example.mycalculator.calculatorapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


 import com.example.mycalculator.calculatorapp.Calculations.AbstractCalculation;
 import com.example.mycalculator.calculatorapp.Calculations.Addition;
 import com.example.mycalculator.calculatorapp.Calculations.Calculation;
 import com.example.mycalculator.calculatorapp.Calculations.Carryable;
 import com.example.mycalculator.calculatorapp.Calculations.Division;
 import com.example.mycalculator.calculatorapp.Calculations.ExponentPower;
 import com.example.mycalculator.calculatorapp.Calculations.Inverse;
 import com.example.mycalculator.calculatorapp.Calculations.Logaritm;
 import com.example.mycalculator.calculatorapp.Calculations.Logaritm10;
 import com.example.mycalculator.calculatorapp.Calculations.Modulus;
 import com.example.mycalculator.calculatorapp.Calculations.Multiplication;
 import com.example.mycalculator.calculatorapp.Calculations.SquareRoot;
 import com.example.mycalculator.calculatorapp.Calculations.Subtraction;

 /**
 *
 * @author J
 */
public enum OperatorMapping {
    PLUS("+", 1, Addition.class),
    MINUS("-", 2, Subtraction.class),
    MUL("*", 3, Multiplication.class),
    DIV("/", 4, Division.class),
    BRACKETOPEN("(", 5, null),
    BRACKETCLOSE(")", 5, null),
    // Operators above should not be rearranged or moved from thier position,
    // thier ordinal (position) in enum will change affecting the program
    EXPONENT("^", 6,ExponentPower.class),
    LOGARITM("log", 7, Logaritm.class),
    LOGARITM10("log10", 8, Logaritm10.class),
    INVERSE("1/x", 9, Inverse.class),
    MODULUS("%", 10, Modulus.class),
    SQUAREROOT("√", 11, SquareRoot.class);
    
    
    
    private final String token;
    private final int order;
    private final Class<? extends Calculation> calculationClass;

    
    OperatorMapping(String token, int order, Class<? extends Calculation> calculationClass){
        this.token = token;
        this.order = order;
        this.calculationClass = calculationClass;
    }
    
    public String getToken(){return this.token;}
    public int getOrder() {return order;}
    public Class<? extends Calculation> getClassForOperator() { return calculationClass; }

     public static int isValidOperator (String t){

         for (OperatorMapping o : OperatorMapping.values()) {
             if (o.getToken().equals(t)) {
                 return o.ordinal();
             }
         }
         return -1;
     }

    public static OperatorMapping fromString (String t){

        for (OperatorMapping o : OperatorMapping.values()) {
            if (o.getToken().equals(t)) {
                return o;
            }
        }
        return null;
    }

}
