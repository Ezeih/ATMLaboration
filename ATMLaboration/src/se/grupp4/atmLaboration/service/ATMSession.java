package se.grupp4.atmLaboration.service;

import se.grupp4.atmLaboration.model.ATMReceipt;

public interface ATMSession
{
	long withdrawAmount(int amount);
	ATMReceipt requestReceipt(long transactionId);
	long checkBalance();
}
