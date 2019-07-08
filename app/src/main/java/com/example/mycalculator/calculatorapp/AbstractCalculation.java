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
public abstract class AbstractCalculation implements Calculation{
    protected double result;
    protected String resultExpression;
    
    public AbstractCalculation(Stack<Double> operands){
        
    }
    
    @Override
    public Double evaluate() {
        return result;
    }

    @Override
    public String getExpression() {
        return resultExpression;
    }

}
