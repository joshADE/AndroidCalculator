/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.mycalculator.calculatorapp.Calculations;

import com.example.mycalculator.calculatorapp.OperatorMapping;

import java.util.Stack;

/**
 *
 * @author J
 */
public class Logaritm extends AbstractCalculation implements Carryable{
    
    public Logaritm(Stack<Double> operands) {
        super(operands);
        result = operands.pop();
        resultExpression =  OperatorMapping.LOGARITM + " ( " + result + " ) ";
        //System.out.println(resultExpression);
        result = Math.log10(result) / Math.log10(2);
    }
    
}
