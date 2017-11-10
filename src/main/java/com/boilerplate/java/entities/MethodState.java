package com.boilerplate.java.entities;
/**
 * This class provide enum for state input.
 * @author love
 *
 */
public enum MethodState {
	Start,
	CreateUser,
	PreLogin,
	Login,
	PreReportFetch,
	ReportQuestion,
	KYC,
	ReportError,
	Report,
	ReportAnalysis,
	ExperianError,
	Registered,
	Validated,
	ExperianAttempt,
	AuthQuestions,
	KYCPending,
	KyCSubmitted,
	ReportGenerated,
	NegotiationInProgress,
	NegotiationCompleted,
	Paying,
	CompletedPayment,
	TradelineStatus,
	WaitingBalance,
	CustomerOffer,
	BankOffer,
	OfferAccepted,
	OfferRejected,
	OfferLetter,
	PaymentSchedule,
	Default,
	Broken,
	ClearanceLetter,
	CIBILUpdate,
	End
	
}
