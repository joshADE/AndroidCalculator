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
    private SimpleCalculator sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.editText);
        resultText = ((TextView) findViewById(R.id.resultsView));

        sc  =  new SimpleCalculator();
        initializeCalculator();

    }

    public void initializeCalculator(){


        inputStack = new Stack<>();

        bracketMatch = 0;
        canUseOp = false;
        canUseDecimal =  true;
        lastInput = "BEG";
    }

    public void printTest(View v){
        Intent intent = new Intent(this, ResultsActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String calc = editText.getText().toString();
        if (lastInput.equals(".")) {
            addToInput("0");
            calc = editText.getText().toString();
        }

        if (isLastAnOperator() || bracketMatch != 0) {


            //("Invalid Expression!Please Retry");


            //initializeCalculator();
            return;
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

    public void numberButtonClick(View v){
        //EditText editText = (EditText) findViewById(R.id.editText);
        String text = new String(((Button)v).getText().toString().trim());

        if (lastInput == null)
            return;


        if (!lastInput.endsWith(") ") && !isLastAConstant())
            addToInput(text);
        else {
            addToInput(" * ");
            addToInput(text);
        }
        canUseOp = true;

    }

    public void decimalButtonClick(View v){
        if (canUseDecimal){
            if (!lastInput.endsWith(") ") && !isLastAConstant()){

                addToInput(".");
            }
            canUseDecimal = false;
            return;
        }

    }


    public void operatorButtonClick(View v) {
        //EditText editText = (EditText) findViewById(R.id.editText);
        String text = ((Button) v).getText().toString().trim();




        if (text.equals(".") && canUseDecimal){
            if (!lastInput.endsWith(") ")){
                canUseDecimal = false;
                addToInput(text);

            }
            return;
        }

        if (text.equals("1/x") || text.equals("log10") || text.equals("log")){

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
            text = "";
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
        //EditText editText = (EditText) findViewById(R.id.editText);
        String text = new String(((Button)v).getText().toString().trim());

        if (lastInput == null)
            return;

        if (isLastAnOperator() || lastInput.trim().equals("("))
            addToInput(text);
        else {
            addToInput(" * ");
            addToInput(text);
        }
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

        String last;
        if (!isLastAnOperator() ){

            if (lastInput.endsWith("( ") )
                return false;

            if (lastInput.endsWith(") "))
                bracketMatch -= 1;

            /*
            if (isLastAConstant())
                return false;
            */
                last = "";
                if (!inputStack.isEmpty())
                    last = inputStack.pop();



                int editLength = input.getText().length();
                input.getText().delete(editLength - last.length(), editLength);
                lastInput = getLastInput();



                if (lastInput == null) {
                    lastInput = "BEG";
                    return false;
                }

                if (last.equals(".")){
                    canUseDecimal = true;
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

        return false;
    }

    public void bracketAdd(View v){
        String text = ((Button) v).getText().toString();
        if (text.equals("(")){

            if (!isLastAnOperator() && !lastInput.endsWith("( ")) {
                //text = " *";
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
                    //text = " ) ";
                    addToInput("0");
                    willAdd = true;
                }

                if (!isLastAnOperator() && !lastInput.endsWith("( ")) {
                    //text = " ) ";
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
            deleteLastInput();
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
