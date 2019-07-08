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
public class Inverse extends AbstractCalculation{
    
    public Inverse(Stack<Double> operands) {
        super(operands);
        result = operands.pop();
        resultExpression = " 1/x(" + result + ")";
        //System.out.println(resultExpression);
        result = Math.pow(result, -1);
    }
    
}
