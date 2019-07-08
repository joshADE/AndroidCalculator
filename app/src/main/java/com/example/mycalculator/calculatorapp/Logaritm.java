/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.mycalculator.calculatorapp;

import java.util.Stack;

/**
 *
 * @author J
 */
public class Logaritm extends AbstractCalculation{
    
    public Logaritm(Stack<Double> operands) {
        super(operands);
        result = operands.pop();
        resultExpression =  "";
        //System.out.println(resultExpression);
        result = Math.log10(result) / Math.log10(2);
    }
    
}
