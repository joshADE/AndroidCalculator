 package com.example.mycalculator.calculatorapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author J
 */
public enum OperatorMapping {
    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    BRACKETOPEN("("),
    BRACKETCLOSE(")"),
    // Operators above should not be rearranged or moved from thier position,
    // thier ordinal (position) in enum will change affecting the program
    EXPONENT("^"),
    LOGARITM("log"),
    LOGARITM10("log10"),
    INVERSE("1/x");
    
    
    
    private final String token;
    //private final Class classAssocition;
    
    OperatorMapping(String token){
        this.token = token;
    }
    
    public String getToken(){return this.token;}
    
public static int isValidOperator(String t){
    
    for (OperatorMapping o : OperatorMapping.values()) {
        if (o.getToken().equals(t)) {
            return o.ordinal();
            
            
        }
        
    }
    return -1;
}

public static int getIndex(String t){
    int ind = -1;
    for (OperatorMapping o : OperatorMapping.values()) {
        if (o.getToken().equals(t)) {
            return o.ordinal();
            
        }
        
    }
    return ind;
    
}

    
    //return EnumUtils.isValidEnum(OperatorMapping.class, myValue)
}
