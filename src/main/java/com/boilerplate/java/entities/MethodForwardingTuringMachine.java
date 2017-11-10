package com.boilerplate.java.entities;


import com.boilerplate.java.collections.BoilerplateSet;

public class MethodForwardingTuringMachine {
	/**
	 * This is the list of inputs
	 */
	 BoilerplateSet <MethodState> inputStateSet = new BoilerplateSet<>(); 
	/**
	 * This is the method state
	 */
	 MethodState methodState ;
	
	/**
	 * This is the success result
	 */
	 MethodState successState;
	
	/**
	 * This is the errorResult
	 */
	 MethodState errorState;
	 /**
	  * This is the state check
	  */
	 private Boolean isStateCheck;
	 
	 /**
	  * This method gets the input state set.
	  * @return the input set
	  */
	public BoilerplateSet<MethodState> getInputStateSet() {
		return inputStateSet;
	}
	/**
	 * This method sets the input state set.
	 * @param inputStateSet the input state Set
	 */
	public void setInputStateSet(BoilerplateSet<MethodState> inputStateSet) {
		this.inputStateSet = inputStateSet;
	}
	/**
	 * This method get the method state.
	 * @return the method state
	 */
	public MethodState getMethodState() {
		return methodState;
	}
	/**
	 * This method set the method state
	 * @param methodState the method state
	 */
	public void setMethodState(MethodState methodState) {
		this.methodState = methodState;
	}
	/**
	 * This method get the success state.
	 * @return the success state
	 */
	public MethodState getSuccessState() {
		return successState;
	}
	/**
	 * This method set the success state
	 * @param successState the success state
	 */
	public void setSuccessState(MethodState successState) {
		this.successState = successState;
	}
	/**
	 * This method get the error state.
	 * @return the error state
	 */
	public MethodState getErrorState() {
		return errorState;
	}
	/**
	 * This method set the error state
	 * @param errorState the error state
	 */
	public void setErrorState(MethodState errorState) {
		this.errorState = errorState;
	}
	/**
	 * This method get the is state check
	 * @return the state check
	 */
	public Boolean getIsStateCheck() {
		return isStateCheck;
	}
	/**
	 * this method set the state check
	 * @param isStateCheck the state check
	 */
	public void setIsStateCheck(Boolean isStateCheck) {
		this.isStateCheck = isStateCheck;
	}
	
	/**
	 * this method gets the generic error update
	 * @return the generic error update
	 */
	public boolean getIsGenericErrorUpdate() {
		return isGenericErrorUpdate;
	}
	/**
	 * this method sets the generic error update
	 * @param isGenericErrorUpdate
	 */
	public void setIsGenericErrorUpdate(boolean isGenericErrorUpdate) {
		this.isGenericErrorUpdate = isGenericErrorUpdate;
	}
	/**
	 * This is the check for generic error update
	 */
	private boolean isGenericErrorUpdate;
	
	/**
	 * this method gets the generic Success update
	 * @return the generic Success update
	 */
	public boolean getIsGenericSuccessUpdate() {
		return isGenericSuccessUpdate;
	}
	/**
	 * this method sets the generic Success update
	 * @param isGenericSuccessUpdate The Generic Success Update
	 */
	public void setIsGenericSuccessUpdate(boolean isGenericSuccessUpdate) {
		this.isGenericSuccessUpdate = isGenericSuccessUpdate;
	}

	/**
	 * This is the check for generic Success update
	 */
	private boolean isGenericSuccessUpdate;
	
	/**
	 * This is the check for user next possible state checks
	 */
	private boolean isUserNextStateCheck;
	
	/**
	 * This is the set of user next states
	 */
	private BoilerplateSet <MethodState> userNextStateSet = new BoilerplateSet<>();

	/**
	 * This method gets the userNextStateSet
	 * @return userNextStateSet The userNextStateSet
	 */
	public BoilerplateSet <MethodState> getUserNextStateSet() {
		return userNextStateSet;
	}
	/**
	 * This method sets the userNextStateSet
	 * @param userNextStateSet The userNextStateSet
	 */
	public void setUserNextStateSet(BoilerplateSet <MethodState> userNextStateSet) {
		this.userNextStateSet = userNextStateSet;
	}
	/**
	 * This method gets the flag for userNextStateSet
	 * @return The flag isUserNextStateCheck
	 */
	public boolean isUserNextStateCheck() {
		return isUserNextStateCheck;
	}
	/**
	 * This method sets the flag isUserNextStateCheck
	 * @param isUserNextStateCheck The flag isUserNextStateCheck
	 */
	public void setUserNextStateCheck(boolean isUserNextStateCheck) {
		this.isUserNextStateCheck = isUserNextStateCheck;
	}

}
