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
public class Subtraction  extends AbstractCalculation{

    public Subtraction(Stack<Double> operands){
        super(operands);
        result = operands.pop();
        resultExpression = operands.peek() + " " + OperatorMapping.MINUS + " " + result;
        //System.out.println(resultExpression);
        result = operands.pop() - result;
    }

}
