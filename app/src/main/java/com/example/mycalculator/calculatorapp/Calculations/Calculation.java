/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.mycalculator.calculatorapp.Calculations;

import java.util.Stack;

/**
 *
 * @author J
 */
public interface Calculation {
   // public String getInputAsString();
    public Double evaluate();
    public String getExpression();
}
