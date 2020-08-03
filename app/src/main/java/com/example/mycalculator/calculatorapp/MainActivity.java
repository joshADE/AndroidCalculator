package com.example.mycalculator.calculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.mycalculator.RESULT_STRING";

    private boolean canUseOp;
    private boolean canUseDecimal;
    private EditText input;
    private TextView resultText;
    private String lastInput;
    private int bracketMatch;
    Stack<String> inputStack;
    private SimpleCalculatorV2 sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.editText);
        resultText = ((TextView) findViewById(R.id.resultsView));

        sc  =  new SimpleCalculatorV2();
        initializeCalculator();

    }

    public void initializeCalculator(){


        inputStack = new Stack<>();

        bracketMatch = 0;
        canUseOp = false;
        canUseDecimal =  true;
        lastInput = "BEG";
    }

    public void equalButtonClick(View v){
        Intent intent = new Intent(this, ResultsActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String calc = editText.getText().toString();

        if (isLastAnOperator() || bracketMatch != 0) {
            resultText.setText("Invalid Expression! Please Retry");
            return;
        }

        if (lastInput.equals(".")) {
            addToInput("0");
        }

        try {
            sc.handleInput(calc);
            ((TextView) findViewById(R.id.resultsView)).setText(sc.getResult());
        }catch (Exception e){
            resultText.setText("Error: " + e.getMessage());

        }

        initializeCalculator();
        input.setText("");
    }

    public void numberButtonClick(View v) {
        //EditText editText = (EditText) findViewById(R.id.editText);
        String text = new String(((Button) v).getText().toString().trim());

        if (lastInput == null)
            return;

        if (lastInput.endsWith(") ") || isLastAConstant()){
            addToInput(" * ");
        }

        addToInput(text);

        canUseOp = true;

    }

    public void decimalButtonClick(View v){
        if (canUseDecimal){
            if (lastInput.endsWith(") ") || isLastAConstant()) {
                addToInput(" * ");
                addToInput("0");
            } else if (lastInput.endsWith("( ") || isLastAnOperator()){
                addToInput("0");
            }

            addToInput(".");
            canUseDecimal = false;
            return;
        }

    }


    public void operatorButtonClick(View v) {

        String text = ((Button) v).getText().toString().trim();

        if (text.equals(".") && canUseDecimal){
            decimalButtonClick(v);
            return;
        }

        if (text.equals("1/x") || text.equals("log10") || text.equals("log") || text.equals("√")){

            if (!isLastAnOperator() && !lastInput.endsWith("( "))
                addToInput(" * ");

            canUseOp = false;
            canUseDecimal = true;
            bracketMatch += 1;
            addToInput( text );
            addToInput(" ( ");

            return;
        }


        if (!canUseOp) {
            return;
        }
        canUseOp = false;
        canUseDecimal = true;
        text = " " + text + " ";
        addToInput(text);
    }

    public void clearButtonClick(View v){
        initializeCalculator();
        input.setText("");
    }

    private void addToInput(String s){
        resultText.setText("");
        lastInput = s;
        input.append(s);
        inputStack.push(s);
    }

    private String getLastInput(){
        if (!inputStack.empty())
            return inputStack.peek();
        return null;
    }

    public void deleteButtonClick(View v){
        if (!deleteLastInput()){
            resultText.setText("Cannot delete anymore!");
        }

    }

    public void constantButtonClick(View v){

        String text = new String(((Button)v).getText().toString().trim());

        if (lastInput == null)
            return;

        if (!isLastAnOperator() && !lastInput.trim().equals("(")) {
            addToInput(" * ");
        }

        addToInput(text);

        canUseOp = true;
    }

    // this method is used  in the deleteLastInput after the right conditions are checked and in negationMethod
    // it will delete whatever is at top of stack and return it as well as from the input text field
    // every time you use this method you must check if lastInput is null and handle case such as setting the last input to a default saying there is nothing in field or stack or BEG -begining
    private String deleteFromStackAndInput(){
        String deletedInput = null;

        if (!inputStack.isEmpty())
            deletedInput = inputStack.pop();



        int editLength = input.getText().length();
        input.getText().delete(editLength - deletedInput.length(), editLength);
        lastInput = getLastInput();

        return deletedInput;

    }


    // doesn't delete '(' open brackets or anything defined as opereator in isLastOperator
    private boolean deleteLastInput(){


        if (isLastAnOperator() || lastInput.endsWith("( ")){
            return false;
        }

        String last = "";
        if (!inputStack.isEmpty()) {
            last = inputStack.pop();
        } else {
            lastInput = "BEG";
            return false;
        }

        if (last.endsWith(") ")) {
            bracketMatch += 1;
        }

        if (last.equals(".")){
            canUseDecimal = true;
        }

        int editLength = input.getText().length();
        input.getText().delete(editLength - last.length(), editLength);

        lastInput = getLastInput();

        if (lastInput == null) {
            lastInput = "BEG";
        }



        // testing////////////////////////////////////////////////////// Doesn't run
        if (isLastAnOperator())
            canUseOp = false;
        /*
        else if (lastInput.equals("."))
            canUseDecimal = false;
        else
            canUseDecimal = false;
        */

        return true;


    }

    public void bracketAdd(View v){
        String text = ((Button) v).getText().toString();
        if (text.equals("(")){

            if (!isLastAnOperator() && !lastInput.endsWith("( ")) {
                addToInput(" *");
            }
            canUseOp = false;
            canUseDecimal = true;
            addToInput(" " + text + " ");
            bracketMatch+=1;
        }else if (text.equals(")")){


            if (bracketMatch > 0) {
                boolean willAdd = false;
                if (lastInput.equals(".")) {
                    addToInput("0");
                    willAdd = true;
                }

                if (!isLastAnOperator() && !lastInput.endsWith("( ")) {
                    willAdd = true;
                }

                if (willAdd) {
                    addToInput(" "+text+" ");
                    bracketMatch -= 1;
                }
            }

        }

    }

    public void negationButtonClick(View v){
        Stack<String> temp = new Stack<>();
        while(isLastAConstant() || isLastANumber() || lastInput.endsWith(") ") || lastInput.equals(".")){
            temp.push(inputStack.peek());
            deleteLastInput(); // call delete last input, syncs deletion with whats is displayed on screen
        }

        if (temp.isEmpty())
            return;


        if (lastInput.equals("-")) {
            deleteFromStackAndInput(); // will delete both '(' and operator strings from the input field unlike deleteLastInput(), but should be used more carefully, may break program
            // deleteFromStackAndInput() may cause lastInput to be null if the very first input was deleted, set it to BEG - to help mark as beginning (debugging)
            if (lastInput == null) {
                lastInput = "BEG";
            }

        }else {
            addToInput(" ( ");
            addToInput("-");
            bracketMatch += 1;
        }

        if (Constants.getValue(temp.peek()) != -1) { //if the number about to be added a constant
            // multiple constant by negative 1
            addToInput("1");
            addToInput(" * ");
        }

        while(!temp.isEmpty()){
            if (temp.peek().endsWith(") ")) {
                bracketMatch -= 1;
            }

            if (temp.peek().equals(".")){
                canUseDecimal = false;
            }
            addToInput(temp.pop());
        }


        // for auto closing brackets not working yet
        /*
        if (lastInput.equals(".")) {
            //addToInput("0");
        }else if(lastInput.endsWith(") ")) {
            addToInput(" ) ");
            bracketMatch -= 1;
        }else{}
        */

    }

    public boolean isLastAnOperator(){
        String last = getLastInput();
        if (last == null) return true;

        char[] arr = last.trim().toCharArray();
        if (arr.length == 0) return true;
        char val =  arr[arr.length - 1];
        if(Character.isDigit(val) || val == '(' || val == ')' || val == '.' || isLastAConstant())
            return false;
        else
            return true;
    }

    public boolean isLastANumber(){
        if (Character.isDigit(lastInput.charAt(lastInput.length() - 1))){
            return true;
        }
        return false;
    }

    public boolean isLastAConstant(){
        if (Constants.getValue(lastInput) == -1){
            return  false;
        }
        return true;
    }





}
