package asyncWorkObservers;

import java.io.Serializable;

public class DispatchObject implements Serializable {
	public int getNumberOne() {
		return numberOne;
	}

	public void setNumberOne(int numberOne) {
		this.numberOne = numberOne;
	}

	public int getNumberTwo() {
		return numberTwo;
	}

	public void setNumberTwo(int numberTwo) {
		this.numberTwo = numberTwo;
	}

	public int getAddResult() {
		return addResult;
	}

	public void setAddResult(int addResult) {
		this.addResult = addResult;
	}

	public int getMultiplyResult() {
		return multiplyResult;
	}

	public void setMultiplyResult(int multiplyResult) {
		this.multiplyResult = multiplyResult;
	}

	public int getSubtractResult() {
		return subtractResult;
	}

	public void setSubtractResult(int subtractResult) {
		this.subtractResult = subtractResult;
	}

	public int getDevideResult() {
		return devideResult;
	}

	public void setDevideResult(int devideResult) {
		this.devideResult = devideResult;
	}

	public int getPowerResult() {
		return powerResult;
	}

	public void setPowerResult(int powerResult) {
		this.powerResult = powerResult;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private int numberOne = 0;
	private int numberTwo = 0;
	
	private int addResult = 0;
	
	private int multiplyResult = 0;
	
	private int subtractResult = 0;
	
	private int devideResult = 0;
	
	private int powerResult = 0;
	
	private String userName;
	
}
