import java.io.*;
import java.util.*;

interface SymbolUser {
	public static final char ADD = '+';
	public static final char SUB = '-';
	public static final char MUL = '*';

	public static final char CROSS = '*';
	public static final char PLUS = '+';
	public static final char MINUS = '-';
}

public class BigInteger {
	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String input = br.readLine();

				if (input.compareTo("quit") == 0) {
					break;
				}

				new Calculator(input);
			} catch (Exception e) {
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

}

class Calculator implements SymbolUser {
	private char OPERATION_TYPE;
	private boolean READY = false;

	private ArrayList<Number> numArr = new ArrayList<Number>();
	private ArrayList<Operator> opArr = new ArrayList<Operator>();

	private Operator unaryBuf;

	public Calculator(final String input) {
		Status status = new Status();

		//입력된 문자를 0번째 문자부터 scanning 하며 숫자 or 연산자를 판단한 뒤 ArrayList에 저장.
		for (int i = 0; i < input.length(); i++) {
			if (Elements.isDigit(input.charAt(i))) {
				Number numBuf = new Number(input, i, Elements.MAX_INPUT_SIZE);
				i += numBuf.getLength() - 1;
				status.readNum(numBuf, this);
				numArr.add(numBuf);

			} else if (Elements.isOperator(input.charAt(i))) {
				Operator opBuf = new Operator(input.charAt(i), i);
				status.readOp(opBuf, this);
				opArr.add(opBuf);
			}
		}
		
		if (numArr.size() >= 3) {
			System.out.println("피연산자는 2개까지 입력 가능합니다.");
			return;
		}
		
		if (READY) {
			calculate();
			READY = false;
		}

	}

	//적절한 숫자와 적절한 연산자가 입력되어 READY = true 가 되었을 때 실행되는 메소
	public void calculate() {
		Number result = null;
		Operation machine = new Operation();

		switch (OPERATION_TYPE) {

		// 숫자의 부호, 연산자의 종류를 종합하여 어떤 연산을 할지 결정한다.
		case ADD:
			if (numArr.get(0).getSign() == numArr.get(1).getSign())
				result = machine.adder(numArr.get(0), numArr.get(1));
			else
				result = machine.subtractor(numArr.get(0), numArr.get(1));
			break;

		case SUB:
			if (numArr.get(0).getSign() == numArr.get(1).getSign())
				result = machine.subtractor(numArr.get(0), numArr.get(1));
			else
				result = machine.adder(numArr.get(0), numArr.get(1));

			break;

		case MUL:
			result = machine.multiplier(numArr.get(0), numArr.get(1));
			break;

		default:
			break;
		}

		if (result != null)
			result.printNum();
	}

	// setters
	public void setREADY() {
		this.READY = true;
	}

	public void setUnaryBuf(Operator op) {
		unaryBuf = op;
	}

	public void setOpType(char opType) {
		OPERATION_TYPE = opType;
	}

	// getters
	public ArrayList<Number> getNumArr() {
		return numArr;
	}

	public ArrayList<Operator> getOpArr() {
		return opArr;
	}

	public Operator getUnaryBuf() {
		return unaryBuf;
	}

	public char getOpType() {
		return OPERATION_TYPE;
	}

}

// ///////////////////////////////////////////////
// Function Class //
// ///////////////////////////////////////////////


//숫자를 읽거나 연산자를 읽었을 때, 현재까지 읽은 값들과 지금 막 읽은 값을 종합하여 적절한 입력 flag를 저장한.
class Status implements SymbolUser {
	public boolean readyNum = false;
	public boolean readyUnary = false;
	public boolean readyOp = false;

	public void readNum(Number num, Calculator calc) {
		if (readyNum == false) {
			readyNum = true;

			if (readyOp == false) {
				if (readyUnary == false) {
					num.setSign(PLUS);
					return;
				} else if (readyUnary == true) {
					num.setSign(calc.getUnaryBuf().getOp());
					calc.setUnaryBuf(null);
					readyUnary = false;
					return;
				}
			} else if (readyOp == true) {
				return;
			}

		} else if (readyNum == true) {
			if (readyOp == false) {
				return;
			} else if (readyOp == true) {
				if (readyUnary == false) {
					num.setSign(PLUS);
					calc.setREADY();
					return;
				} else if (readyUnary == true) {
					num.setSign(calc.getUnaryBuf().getOp());
					calc.setREADY();
					readyUnary = false;
					return;
				}
				readyOp = false;
			}

		}

	}

	public void readOp(Operator op, Calculator calc) {
		if (readyNum == false) {
			if (readyOp == false) {
				if (readyUnary == false) {
					if (Elements.isUnary(op.getOp())) {
						op.setUnary(op.getOp());
						calc.setUnaryBuf(op);
						readyUnary = true;
						return;
					}
				} else if (readyUnary == true) {
					return;
				}
			} else if (readyOp == true) {
				return;
			}
		} else if (readyNum == true) {
			if (readyOp == false) {
				if (readyUnary == false) {
					calc.setOpType(op.getOp());
					readyOp = true;
					return;
				} else if (readyUnary == true) {
					return;
				}
			} else if (readyOp == true) {
				if (readyUnary == false) {
					if (Elements.isUnary(op.getOp())) {
						op.setUnary(op.getOp());
						calc.setUnaryBuf(op);
						readyUnary = true;
						return;
					}
				} else if (readyUnary == true) {
					return;
				}

			}

		}

	}

}


//연산을 정의한 클래스. one digit 연산과, one digit 연산을 반복하여 사용하는 일반 연산 메소드가 있다. 
class Operation implements SymbolUser {

	public static Operand oneDigitAdder(final Operand op1, final char op2) {
		int num1, num2;

		// char형 변수를 int형 변수로 변환. ('0'의 ASCII Code = 48)
		// 피연산자의 값이 null일 때는 강제적으로 숫자 0을 지정 
		if (op1.getDigit() == 0) {
			num1 = 0;
		} else {
			num1 = op1.getDigit() - 48;
		}

		if (op2 == 0) {
			num2 = 0;
		} else {
			num2 = op2 - 48;
		}

		// int로 변환한 상태에서 연산 수행.
		int sum = num1 + num2 + op1.getCarry();

		// 연산 결과를 Operand type로 변환.
		char resultDigit = (char) (sum % 10 + 48);
		int carryUp = sum / 10;

		return new Operand(resultDigit, carryUp);
	}

	public Number adder(final Number num1, final Number num2) {

		Number largeNum, smallNum;
		if (num1.isBiggerThan(num2)) {
			largeNum = num1;
			smallNum = num2;
		} else {
			largeNum = num2;
			smallNum = num1;
		}

		Number numResult = new Number(largeNum.getLength() + 1);

		int carry = 0;
		Operand operandResult;

		for (int i = 0; i < largeNum.getLength(); i++) {
			Operand operandBuf = new Operand(largeNum.getDigit(i), carry);

			operandResult = oneDigitAdder(operandBuf, smallNum.getDigit(i));

			numResult.setDigit(operandResult.getDigit(), i);
			carry = operandResult.getCarry();
		}

		if (carry == 1) {
			numResult.setDigit('1', largeNum.getLength());
		} else {
			numResult.setLength(largeNum.getLength());
		}

		numResult.setSign(num1.getSign());

		return numResult;
	}

	public static Operand oneDigitSubtractor(final Operand op1, final char op2) {
		int num1, num2;

		// char형 변수를 int형 변수로 변환. ('0'의 ASCII Code = 48)
		// 피연산자의 값이 null일 때는 강제적으로 숫자 0을 지정
		if (op1.getDigit() == 0) {
			num1 = 0;
		} else {
			num1 = op1.getDigit() - 48;
		}

		if (op2 == 0) {
			num2 = 0;
		} else {
			num2 = op2 - 48;
		}

		// int로 변환한 상태에서 연산 수행.
		int sub = num1 - num2 + op1.getCarry();

		// 연산 결과를 Operand type로 변환.
		char resultDigit;
		int carryDown;

		if (sub >= 0) {
			resultDigit = (char) (sub + 48);
			carryDown = 0;
		} else {
			resultDigit = (char) ((10 + sub) + 48);
			carryDown = -1;
		}

		return new Operand(resultDigit, carryDown);
	}

	public Number subtractor(final Number num1, final Number num2) {

		Number largeNum, smallNum;
		if (num1.isBiggerThan(num2)) {
			largeNum = num1;
			smallNum = num2;
		} else {
			largeNum = num2;
			smallNum = num1;
		}

		Number numResult = new Number(largeNum.getLength());

		int carry = 0;
		Operand operandResult;

		for (int i = 0; i < largeNum.getLength(); i++) {
			Operand operandBuf = new Operand(largeNum.getDigit(i), carry);

			operandResult = oneDigitSubtractor(operandBuf, smallNum.getDigit(i));

			numResult.setDigit(operandResult.getDigit(), i);
			carry = operandResult.getCarry();
		}

		if (largeNum.getSign() != smallNum.getSign()) {
			numResult.setSign(largeNum.getSign());
		} else {
			if (largeNum.getSign() == PLUS) {
				if (largeNum == num1)
					numResult.setSign(PLUS);
				else
					numResult.setSign(MINUS);
			} else {
				if (largeNum == num1)
					numResult.setSign(MINUS);
				else
					numResult.setSign(PLUS);
			}
		}

		for (int i = 0; i < numResult.getLength(); i++) {
			if (numResult.getDigit(i) != '0') {
				break;
			} else if (i == numResult.getLength() - 1) {
				numResult.setSign(PLUS);
			}
		}

		return numResult;

	}

	public static Operand oneDigitMultiplier(final Operand op1, final char op2) {
		int num1, num2;

		// char형 변수를 int형 변수로 변환. ('0'의 ASCII Code = 48)
		// 피연산자의 값이 null일 때는 강제적으로 숫자 0을 지정
		if (op1.getDigit() == 0) {
			num1 = 0;
		} else {
			num1 = op1.getDigit() - 48;
		}

		if (op2 == 0) {
			num2 = 0;
		} else {
			num2 = op2 - 48;
		}

		// int로 변환한 상태에서 연산 수행.
		int mul = num1 * num2 + op1.getCarry();

		// 연산 결과를 Operand type로 변환.
		char resultDigit = (char) (mul % 10 + 48);
		int carryUp = mul / 10;

		return new Operand(resultDigit, carryUp);
	}

	public Number multiplier(final Number num1, final Number num2) {

		Number largeNum, smallNum;
		if (num1.isBiggerThan(num2)) {
			largeNum = num1;
			smallNum = num2;
		} else {
			largeNum = num2;
			smallNum = num1;
		}

		Number numResult = new Number(largeNum.getLength() * 2);
		Number stepResult = new Number(largeNum.getLength() * 2);

		int carry = 0;
		Operand operandResult;

		for (int i = 0; i < smallNum.getLength(); i++) {

			for (int k = 0; k < i; k++) {
				stepResult.setDigit('0', k);
			}
			for (int j = 0; j < largeNum.getLength(); j++) {
				Operand operandBuf = new Operand(largeNum.getDigit(j), carry);

				operandResult = oneDigitMultiplier(operandBuf,
						smallNum.getDigit(i));

				stepResult.setDigit(operandResult.getDigit(), j + i);
				carry = operandResult.getCarry();
			}

			if (carry != 0) {
				stepResult.setDigit((char)(carry+48), largeNum.getLength() + i);
			}

			numResult = this.adder(numResult, stepResult);
		}

		if (num1.getSign() == num2.getSign()) {
			numResult.setSign(PLUS);
		} else {
			numResult.setSign(MINUS);
		}

		return numResult;
	}
}

// ///////////////////////////////////////////////
// Object Class //
// ///////////////////////////////////////////////

// Operation 클래스에서 쓰이는 피연산자 object class
class Operand {
	private int carry;
	private char digit;

	public Operand(final char digit, final int carry) {
		this.digit = digit;
		this.carry = carry;
	}

	// setters
	public void setCarry(final int c) {
		carry = c;
	}

	public void setDigit(final char ch) {
		digit = ch;
	}

	// getters
	public int getCarry() {
		return carry;
	}

	public char getDigit() {
		return digit;
	}

}

// operator와 number가 공통적으로 가지는 속성을 상위 클래스 Elements로 정
class Elements implements SymbolUser {
	public static final int MAX_INPUT_SIZE = 100;

	private int indexInStr = 0;
	private int length = 0;

	// constructor
	public Elements() {
	}

	// discriminator
	public static boolean isDigit(final char ch) {
		if (ch >= '0' && ch <= '9')
			return true;
		else
			return false;
	}

	public static boolean isOperator(final char ch) {
		if (ch == CROSS || ch == PLUS || ch == MINUS)
			return true;
		else
			return false;
	}

	public static boolean isUnary(final char ch) {
		if (ch == PLUS || ch == MINUS)
			return true;
		else
			return false;
	}

	// setters
	public void setIndex(final int index) {
		this.indexInStr = index;
	}

	public void setLength(final int length) {
		this.length = length;
	}

	// getters
	public int getIndex() {
		return indexInStr;
	}

	public int getLength() {
		return length;
	}

}

class Operator extends Elements {
	private char operator;
	private boolean isUnary = false;
	private char unaryType;

	// constructor
	public Operator(final char operator, final int indexInStr) {
		this.operator = operator;
		setIndex(indexInStr);
		setLength(1);
	}

	// setters
	public void setUnary(final char sign) {
		if (super.isUnary(sign)) {
			this.unaryType = sign;
			this.isUnary = true;
		}
		return;
	}

	// getters
	public char getOp() {
		return operator;
	}

	public boolean isThisUnary() {
		return isUnary;
	}

	public char getUnary() {
		return unaryType;
	}
}

class Number extends Elements {
	private char[] num;
	private char sign = PLUS;

	// constructors
	public Number(final String input, final int indexInStr, final int size) {
		if (size > MAX_INPUT_SIZE)
			num = new char[size];
		else
			num = new char[MAX_INPUT_SIZE];

		int length = 0;

		// pre-scanning for deciding length
		for (int i = 0; indexInStr + i < input.length()
				&& Elements.isDigit(input.charAt(indexInStr + i)); i++) {
			length++;
		}

		for (int i = 0; i < length; i++) {
			num[i] = input.charAt(indexInStr + length - 1 - i);
		}

		setLength(length);
		setIndex(indexInStr);
	}

	public Number(final int size) {

		if (size > MAX_INPUT_SIZE)
			num = new char[size];
		else
			num = new char[MAX_INPUT_SIZE];

		setLength(size);

	}

	public Number() {
	}

	// setters
	public void setDigit(final char digit, final int index) {
		num[index] = digit;
	}

	public void setSign(char sign) {
		this.sign = sign;
	}

	// getters
	public char[] getNum() {
		return num;
	}

	public char getDigit(final int index) {
		return num[index];
	}

	public char getSign() {
		return sign;
	}

	// methods
	public void printNum() {
		
		for (int i = getLength() - 1; i >= 0; i--) {
			if (num[i] == '0') {
				if (i==0) {
					System.out.println("0");
					return;
				}
				continue;
			} else {
				if (sign == MINUS) {
					System.out.print("-");
				}
				for (int j = i; j >= 0; j--) {
					System.out.printf("%c", num[j]);
				}
				System.out.println();
				return;
			}
		}	
	}

	public boolean isBiggerThan(Number otherNum) {
		if (this.getLength() > otherNum.getLength())
			return true;
		else if (this.getLength() == otherNum.getLength()) {
			for (int i = 0; i < this.getLength(); i++) {
				if (this.getDigit(this.getLength() - 1 - i) > otherNum
						.getDigit(otherNum.getLength() - 1 - i)) {
					return true;
				} else if (this.getDigit(this.getLength() - 1 - i) < otherNum
						.getDigit(otherNum.getLength() - 1 - i)) {
					return false;
				} else
					continue;
			}
		}
		return false;
	}

}
