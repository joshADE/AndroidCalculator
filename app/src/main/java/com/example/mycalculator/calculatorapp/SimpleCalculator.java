package com.example.mycalculator.calculatorapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




import java.util.LinkedList;
import java.util.*;

/**
 *
 * @author J
 */
public class SimpleCalculator {
    private List<String> history = new LinkedList<>();
    private final int historySize = 20;
    Stack<OperatorMapping> operatorStack = new Stack<>();
    Stack<Double> operandStack = new Stack<>();
    private int currentHistoryIndex;
    private int carry = 0;
    public static final int DEFAULT_DECIMAL_PLACES = 9;
    private int decimalPlace = DEFAULT_DECIMAL_PLACES;


    public SimpleCalculator() {
        this.currentHistoryIndex = 0;
    }



    public void setDecimalPlace(int n){
        if (n > 0)
            decimalPlace = n;
    }

    public int getDecimalPlaces(){
        return decimalPlace;
    }
    
    public boolean handleInput(String input) throws Exception{
        //validate
        
        String[] tokens = input.split(" ");
        
        // used for stroring the ordinal of the operator 
        // from the current token or at the top of the operatorStack 
        int operation = -1;
        double constval = -1; 
        
        for (String token: tokens){
           if (token.length() == 0){
           
           }else if ((operation = OperatorMapping.isValidOperator(token)) != - 1){
                
               
                //get the constant in OperatorMapping that represents the token
                OperatorMapping om = OperatorMapping.values()[operation];
            
                
                
               Calculation c;
                
                
               if (null != om)switch (om) {
                   case PLUS:
                   case MINUS:
                       while(!operatorStack.isEmpty() && operatorStack.peek().ordinal() < 4){
                           //System.out.println("token:"+operatorStack.peek().getToken()+"ordinal:" + operatorStack.peek().ordinal());
                           c = process(operandStack, operatorStack);
                           operandStack.push(c.evaluate());
                       }
                       operatorStack.push(om);
                       break;
                   case MUL:
                   case DIV:
                       while(!operatorStack.isEmpty() && (operation = operatorStack.peek().ordinal()) >= 2 && operation <= 3 ){
                           //System.out.println("token:"+operatorStack.peek().getToken()+"ordinal:" + operatorStack.peek().ordinal());
                           c = process(operandStack, operatorStack);
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
                       operatorStack.push(om);
                       carry++;
                       break;

                   default:
                       //operatorStack.push(om);
                       //carry++;
                       break;
               }
               
           }else if (token.equals("ans")){
               
               operandStack.push(getResultDouble());
               
            }else{
               double value = Constants.getValue(token);
               
               if (value == -1.0){
                   
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
    
    private Calculation process(Stack<Double> operands, Stack<OperatorMapping> operators){
        OperatorMapping om = operators.pop();
        
        Calculation c = null;
        if (null != om)
            switch (om) {
            case PLUS:
                c = new Addition(operands);
                break;
            case MINUS:
                c = new Subtraction(operands);
                break;
            case MUL:
                c =  new Multiplication(operands);
                break;
            case DIV:
                c = new Division(operands);
                break;
            case EXPONENT:
                c = new ExponentPower(operands);
                break;
            case LOGARITM10:
                c = new Logaritm10(operands);
                break;
                
            case LOGARITM:
                c = new Logaritm(operands);
                break;
            case INVERSE:
                c = new Inverse(operands);
                break;
            default:
                break;
        }
   
        return c;

    }
    
    private void performCarryCheck(){
        boolean shouldExit = false;
        while (carry > 0 && !shouldExit){ 
                switch(operatorStack.peek()){
                    case EXPONENT: 
                    case LOGARITM10:
                    case LOGARITM:
                        
                        Calculation c = process(operandStack, operatorStack);
                        operandStack.push(c.evaluate());
                        carry--;
                    break;
                    
                    
                    
                default: 
                    shouldExit = true;
                    break;

                }
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
