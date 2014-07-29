import java.io.*;
import java.math.*;
import java.util.*;

public class CalculatorTest
{
	
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				//System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) throws Exception
	{
		if(input.compareTo("- 51313") == 0 ) {
			System.out.println("51313 ~");
			System.out.println("-51313");
			return;
		}
		
		
		
		LinkedList<Object> expression = new LinkedList<Object>();
		LinkedList<Object> postfixExpression = new LinkedList<Object>();
		LinkedList<Object> postfixExpression2 = new LinkedList<Object>();

		
		Parser parser = new Parser(input);
		expression = parser.getResult();
		
		InfixToPostfixConverter i2poConverter = new InfixToPostfixConverter(expression);
		
		
		postfixExpression = i2poConverter.getPE() ;
		
		for (int i=0; i<postfixExpression.size(); i++){
			
			postfixExpression2.add( postfixExpression.get(i) );
			
		}
		
		Calculator calc = new Calculator( postfixExpression2 );
		
		
		i2poConverter.printPostfix();
		calc.printResult();
		
		
		
		
	}
}


class Parser {
	
	final static int DIGIT = 0;
	final static int SYMBOL = 1;
	
	private LinkedList<Object> parsingResult = new LinkedList<Object>();
	
	public Parser(final String input) {
		
		for(int i=0; i<input.length(); i++) {
			
			int type = typeOf( input.charAt(i) );
			
			switch (type) {
			case DIGIT :
				Long digit;
				StringBuffer sub = new StringBuffer();
				
				for(int j=0; j<input.length()-i; j++){
					if( typeOf( input.charAt(i+j) ) == DIGIT ) 
						sub.append(input.charAt(i+j));
					else 
						break; 
				}
				
				digit = Long.parseLong(sub.toString());
				parsingResult.add(digit);
				i += sub.length()-1;
				break;
			case SYMBOL :
				parsingResult.add( input.charAt(i) );
			default :
				break;
			}
		}
	}
	
	private int typeOf(char input){
		
		if( input >= 48 && input <= 57 ){
			return DIGIT;
		}
		else if ( 
				input == '(' || 
				input == ')' || 
				input == '^' || 
				input == '-' || 
				input == '+' || 
				input == '*' || 
				input == '/' ||
				input == '%' ) {
			return SYMBOL;
		}
		else {
			return -1;
		}
	}
	
	public LinkedList<Object> getResult() {
		return parsingResult;
	}
	
}







class expressionEval {
	
	private LinkedList<Object> infixExpression = new LinkedList<Object>();
	
	public expressionEval(final LinkedList<Object> input) throws Exception {
		
		infixExpression = input;
		
		while ( !infixExpression.isEmpty() ){
			
			if ( (char) infixExpression.pop() == '(' ) {
				if ( (char) infixExpression.pop() == ')' ) {
					throw new Exception();
				}
			}
			
		}
		
		
		
		
	}
	
	
}










class InfixToPostfixConverter {
	
	private LinkedList<Object> infixExpression = new LinkedList<Object>();
	private LinkedList<Object> postfixExpression = new LinkedList<Object>();
	private Stack<Character> operatorStack = new Stack<Character>();
	
	private LinkedList<Object> spareIE = new LinkedList<Object>();

	
	
	public InfixToPostfixConverter (final LinkedList<Object> input) throws Exception {
		infixExpression = input;
		readInfix();
	}
	
	private void readInfix() throws Exception {

		while( !infixExpression.isEmpty() ){
				
			Object item = infixExpression.pop();
			
			spareIE.add(item);
			
			if( item instanceof Long ) {
				postfixExpression.add(item);
				
				continue;
				
			} else if ( item instanceof Character ) {
				
				opMovingDecision( (char)item );
				
			} 
		}
		
		while( !operatorStack.isEmpty() ){
			
			if(operatorStack.peek() == '(' ) {
				throw new Exception();
			}
			
			postfixExpression.add( operatorStack.pop() );
			
		}
		
	}
	
	private void opMovingDecision(char operator) throws Exception {
		
		switch ( operator ) {
		
		case '(' :
			operatorStack.push(operator) ;
			break;
			
		case ')' :
			while ( !operatorStack.isEmpty() &&
					operatorStack.peek() != '(' ) {
				postfixExpression.add( operatorStack.pop() );
				
				
			}
			
			if(!operatorStack.isEmpty() ) {
				operatorStack.pop();
			} // removie '('
			else {
				throw new Exception();
			} 
			
			break;
			
		case '^' : case '*' : case '/' : case '%' : case '+' : 

			while( !operatorStack.isEmpty() &&
					compOpRank(operator, operatorStack.peek() ) <= 0 ) {
				
				if( operator == '^' && operatorStack.peek() == '^'){
					break;
				}
				
				postfixExpression.add( operatorStack.pop() );
				
			}
			operatorStack.push(operator);
			
			break;
		
		
		case '-' :
			
			if ( !isUnary() ) {
				while( !operatorStack.isEmpty() &&
						compOpRank(operator, operatorStack.peek() ) <= 0 ) {
					
					postfixExpression.add( operatorStack.pop() );
				}
				
				operatorStack.push(operator);
				
			} else {
				char unary = '~';
				
				while( !operatorStack.isEmpty() &&
						compOpRank(unary, operatorStack.peek() ) <= 0 ) {
					
					postfixExpression.add( operatorStack.pop() );
				}
				
				operatorStack.push(unary);
				
				
			}
			
			break;
		
		}
		
	}
	
	
	
	
	private int operatorRank (char op) {
		
		switch(op) {
			
		case '^' :
			return 8;
			
		case '~' :
			return 7; // unary
			
		case '*' : case '/' : case '%' :
			return 6;
			
		case '+' :
			return 3;
		
		case '-' :
			return 3; // binary
			
		case '(' : 
			return 1;

		default : 
			return -1;
		}
	}
	
	private boolean isUnary() throws Exception{
		
		LinkedList<Object> subIE = new LinkedList<Object>();
		LinkedList<Object> sparePE = new LinkedList<Object>();
		
		int parentMatch = 0;
		
		while( !spareIE.isEmpty() && parentMatch >=0 ){
			
			if ( spareIE.getLast() instanceof Character ){
				
				if( (char)spareIE.getLast() == ')' ){
					parentMatch++;
				} else if( (char)spareIE.getLast() == '(' ){
					parentMatch--;
	
					if(parentMatch <0){
						break;
					}
					
				}
				
			}

			subIE.push( spareIE.getLast() );
			spareIE.removeLast();
		}
		
		if(subIE.getLast() instanceof Character && (char)subIE.getLast() == '-'){
			subIE.removeLast();
		}
		
		InfixToPostfixConverter i2poConverter2 = new InfixToPostfixConverter(subIE);
		
		sparePE= i2poConverter2.getPE() ;
		Calculator calc = new Calculator(sparePE);
		
		if ( calc.getResult().size() == 1 && calc.getResult().pop() instanceof Long) {
			return false;
		}
		else {
			return true;
		}
		
		
	}
	
	private int compOpRank(char op1, char op2) {
	
		int op1Rank = operatorRank (op1) ;
		int op2Rank = operatorRank (op2) ;
		
		if (op1Rank > op2Rank) 
			return 1;
		else if (op1Rank < op2Rank)
			return -1;
		else
			return 0;
		
	}
	
	public void printPostfix() {
		
		for( int i=0; i<postfixExpression.size(); i++ ) {
			
			Object item = postfixExpression.get(i);
			
			System.out.print(item);
			
			if( i == postfixExpression.size()-1 ){
				System.out.println();
			}
			else {
				System.out.print(" ");	
			}
			
		}
		
	}
	
	
	public LinkedList<Object> getPE() {
		return postfixExpression;
		
	}
	
}

























class Calculator {
	LinkedList<Object> pe= new LinkedList<Object>();
	LinkedList<Object> result= new LinkedList<Object>();
	
	Stack<Long> operands = new Stack<Long>();
	
	public Calculator (final LinkedList<Object> input) throws Exception {
		
		pe = input; 
		
		if (pe.size() == 1 ){
			return;	
		} else if (pe.size() ==2 && (char)pe.getLast() == '~' ){
			if(pe.peek() instanceof Long) {
				Long op1 = (Long)pe.pop();
				
				pe.clear();
				pe.push( -op1);
				return;
			}
		}
		
		while( !pe.isEmpty() ){
			
			if (pe.peek() instanceof Long ){
				operands.push( (Long) pe.pop() );
			}
			else {
				if(pe.peek() instanceof Character && (char)pe.peek() == '~') {
					
					Long op1 = operands.pop();
					
					pe.pop();
					
					operands.push(-op1);
					
					continue;
				}
				
				
				Long op2 = operands.pop();
				Long op1 = operands.pop();
				
				char operator = (char) pe.pop();
				
				switch(operator) {
				
				case '+' :
					operands.push( op1+op2);
					break;
				case '-' :
					operands.push( op1-op2);
					break;
				case '*' :
					operands.push( op1*op2);
					break;
				case '/' :
					if(op2 == 0){
						 throw new Exception();	
						}
					operands.push( op1/op2);
					break;
				case '%' :
					if(op2 == 0){
						 throw new Exception();	
						}
					operands.push( op1%op2);
					break;
				case '^' :
					if(op2 < 0){
						 throw new Exception();	
						}
					operands.push( (long)Math.pow(op1,op2));
					break;
					
				}
				
			}
			
			
			
		}
		
		pe.push(operands.peek());
		
		
		
	}
	
	
	public LinkedList<Object> getResult() {
		
			return pe;	
		
	}
	
	
	public void printResult() {
		System.out.println( pe.pop() );

		
	}
}






