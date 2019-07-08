/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.mycalculator.calculatorapp;

/**
 *
 * @author J
 */
public enum Constants {
    
    ANSWER("ans", 0),
    PI("pi",Math.PI),
    EULERS("e", Math.E);
    //UNKNOWN("unk", -1);
    
    
    private final String token;
    private final double val;
    
    Constants(String token, double value){
        this.token = token;
        val = value;
    }
    
    
    public String getToken(){return this.token;}
    public double getValue(){return this.val;}

public static double getValue(String t){
    
    for (Constants o : Constants.values()) {
        if (o.getToken().equals(t)) {
            return o.getValue();
                       
        }
        
    }
    return -1;
}

}
