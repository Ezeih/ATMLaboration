package se.grupp4.atmLaboration.service;

import se.grupp4.atmLaboration.model.BankReceipt;

public interface Bank
{
	String getBankId();
	long getBalance(String accountHolderId);
	long withdrawAmount(int amount);
	BankReceipt requestReceipt(long transactionId);

}

