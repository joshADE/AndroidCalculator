package com.example.mycalculator.calculatorapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import android.support.annotation.NonNull;

import com.example.mycalculator.calculatorapp.Calculations.Calculation;
import com.example.mycalculator.calculatorapp.Calculations.Carryable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author J
 */
public class SimpleCalculatorV2 {
    private List<String> history = new LinkedList<>();
    private final int historySize = 20;
    Stack<OperatorMapping> operatorStack = new Stack<>();
    Stack<Double> operandStack = new Stack<>();
    private int currentHistoryIndex;
    private int carry = 0;
    public static final int DEFAULT_DECIMAL_PLACES = 9;
    private int decimalPlace = DEFAULT_DECIMAL_PLACES;
    private Map<String, Class<? extends Calculation>> calcMap;
    private Map<String, Double> constMap;
    Comparator<Class> comparator;


    public SimpleCalculatorV2() {

        this.currentHistoryIndex = 0;
        calcMap = new LinkedHashMap<>(OperatorMapping.values().length + 1);
        for (OperatorMapping o: OperatorMapping.values()){
            calcMap.put(o.getToken(), o.getClassForOperator());
        }
        constMap = new LinkedHashMap<>(Constants.values().length + 1);
        for (Constants c: Constants.values()){
            constMap.put(c.getToken(), c.getValue());
        }

        comparator = new Comparator<Class>() {
            @Override
            public int compare(Class a, Class b) {
                return a.getName().compareTo(b.getName());
            }
        };
    }



    public void setDecimalPlace(int n){
        if (n > 0)
            decimalPlace = n;
    }

    public int getDecimalPlaces(){
        return decimalPlace;
    }
    
    public boolean handleInput(String input) throws Exception{
        // should validate here
        
        String[] tokens = input.split(" ");
        
        // used for storing the ordinal of the operator
        // from the current token or at the top of the operatorStack 
        int operation = -1;
        
        for (String token: tokens){

           if (token.length() == 0){
               // check if the token is an empty string

               // do nothing

           }else if ((operation = OperatorMapping.isValidOperator(token)) != - 1){
               // check if the token is an operator
               
                //get the operator in OperatorMapping that represents the token
                OperatorMapping om = OperatorMapping.values()[operation];
            
                
                
               Calculation c;
                
                
               if (null != om)
                   switch (om) {
                   case PLUS:
                   case MINUS:
                       while(!operatorStack.isEmpty() && operatorStack.peek().ordinal() < 4){
                           //System.out.println("token:"+operatorStack.peek().getToken()+"ordinal:" + operatorStack.peek().ordinal());
                           c = process(operandStack, operatorStack);
                           //if (c == null) break;
                           operandStack.push(c.evaluate());
                       }
                       operatorStack.push(om);
                       break;
                   case MUL:
                   case DIV:
                       while(!operatorStack.isEmpty() && (operation = operatorStack.peek().ordinal()) >= 2 && operation <= 3 ){
                           //System.out.println("token:"+operatorStack.peek().getToken()+"ordinal:" + operatorStack.peek().ordinal());
                           c = process(operandStack, operatorStack);
                           //if (c == null) break;
                           operandStack.push(c.evaluate());
                       }
                       operatorStack.push(om);
                       break;
                   case BRACKETOPEN:
                       operatorStack.push(om);
                       break;
                   case BRACKETCLOSE:
                       while (operatorStack.peek().compareTo(OperatorMapping.BRACKETOPEN) != 0){
                           c = process(operandStack, operatorStack);
                           //if (c == null) break;
                           operandStack.push(c.evaluate());
                       }  
                       operatorStack.pop();
                       //process the ^ on top of stack
                       /*
                       while (carry > 0 && operatorStack.peek() == OperatorMapping.EXPONENT){
                       c = process(operandStack, operatorStack);
                       operandStack.push(c.evaluate());
                       carry--;
                       }
                       */
                       // above moved into performCarryCheck function
                       performCarryCheck();
                       break;
                       
                   case LOGARITM:
                   case LOGARITM10:
                   case EXPONENT:
                   case INVERSE:
                   case SQUAREROOT:
                   case MODULUS:
                       operatorStack.push(om);
                       carry++;
                       break;
                   default:
                       if (operatorIsInstanceOfInterface(om, Carryable.class)) {
                           operatorStack.push(om);
                           carry++;
                       }else {
                           throw new Exception("Operation " + om.getToken() + " not implemented");
                       }
               }
               
           }else if (token.equals("ans")){
               // check if the token is the ans keyword meaning the previous answer
               operandStack.push(getResultDouble());
            }else{
               // check if the token is a constant defined in the Constants Enum
               double value = Constants.getValue(token);
               
               if (value == -1.0){
                   // check if the token is a number
                   try{
                    value = Double.parseDouble(token);
                   }catch(Exception e){
                       throw new Exception("There was a problem parsing the input/operation", e);
                   }
                }
               
               operandStack.push(value);
               /*
               while (carry > 0 && operatorStack.peek() == OperatorMapping.EXPONENT){
                   Calculation c = process(operandStack, operatorStack);
                   operandStack.push(c.evaluate());
                   carry--;
               }
               */
               // ^ moved into performCarryCheck function
               performCarryCheck();
           }
        }
        
        
        Calculation c;
        while (!operatorStack.isEmpty()){
                
                c = process(operandStack, operatorStack);
                //if (c == null) break;
                operandStack.push(c.evaluate());
               
        }
	double res = operandStack.pop();
        //addToHistory(input + " = " + res);
        
        int res2 = (int)(res);
        
        if (res - res2 == 0)
            addToHistory(input + " = " + res2);
        else
            addToHistory(input + " = " + Math.round(res * Math.pow(10, decimalPlace))/(Math.pow(10.0,decimalPlace)));

        


        //System.out.println("Result: " + operandStack.pop());


        //addToHistory(input + " = " + operandStack.pop());
        //System.out.println("Result: " + operandStack.pop());

        return true;
    }

    public boolean operatorIsInstanceOfInterface(OperatorMapping operator, Class supertype){
        Class cls = operator.getClassForOperator();
        return (cls != null && Arrays.binarySearch(cls.getInterfaces(), supertype, comparator) >= 0);
    }
    
    private Calculation process(Stack<Double> operands, Stack<OperatorMapping> operators) throws Exception{
        Calculation c = null;
        OperatorMapping om = operators.pop();


        try {
            c = om.getClassForOperator().getConstructor(operands.getClass()).newInstance(operands);
            return c;

        } catch (Exception e){
            //System.out.println("Class for operation not found");
            //return null;
            throw new Exception("Class for operation not found");

        }

    }
    
    private void performCarryCheck() throws Exception{
        boolean shouldExit = false;

        while (carry > 0 && !shouldExit){
                OperatorMapping om = operatorStack.peek();


                if (operatorIsInstanceOfInterface(om, Carryable.class)){
                     Calculation c;
                     c = process(operandStack, operatorStack);
                     // if (c == null) return;
                     operandStack.push(c.evaluate());
                     carry--;
                 }else{
                     shouldExit = true;
                 }
                 /*
                switch(operatorStack.peek()){
                    case EXPONENT: 
                    case LOGARITM10:
                    case LOGARITM:
                    case SQUAREROOT:
                    case MODULUS:
                        Calculation c;

                            c = process(operandStack, operatorStack);
                            if (c == null) return;
                            operandStack.push(c.evaluate());
                            carry--;
                    break;
                default: 
                    shouldExit = true;
                    break;

                }

                  */
        }
    
    }
    
    
    
    private void addToHistory(String c){
        if (history.size() >= historySize){
            history.remove(0);
            currentHistoryIndex--;
        }
        
        history.add(c);
        currentHistoryIndex++;
    }

    public String getResult(){
        if (history.size() > 0)
            return history.get(history.size() - 1);
        
        return "ans = 0";
        
    }
    
     public Double getResultDouble(){
        return Double.parseDouble(getResult().split("=")[1]);
    }
    
    public List<String> getFullHistory(){
        return history;
    }

    
    
                
    
    
}
