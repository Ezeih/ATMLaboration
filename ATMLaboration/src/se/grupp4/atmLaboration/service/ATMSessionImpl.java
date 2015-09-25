package se.grupp4.atmLaboration.service;

import se.grupp4.atmLaboration.exception.ATMException;
import se.grupp4.atmLaboration.model.ATMCard;
import se.grupp4.atmLaboration.model.ATMReceipt;
import se.grupp4.atmLaboration.model.BankReceipt;

public final class ATMSessionImpl extends AbstractATMSession
{
	private int withdrawAmountCallCounter = 0;
	private int checkBalanceCallCounter = 0;

	public ATMSessionImpl(ATMCard atmCard, Bank bank)
	{
		super(atmCard, bank);
	}

	@Override
	public long withdrawAmount(int amount)
	{
		long balance = bank.getBalance(atmCard.getAccountHolderId());

		if ((amount > 100) && (amount < 10000)
				&& (amount % 100 == 0) && (withdrawAmountCallCounter < 1)
				&& (balance >= amount))
		{
			balance = bank.withdrawAmount(amount);
			withdrawAmountCallCounter++;

			return balance;
		}
		throw new ATMException("can not withdraw money");
	}

	@Override
	public ATMReceipt requestReceipt(long transactionId)
	{
		BankReceipt bankReceipt = bank.requestReceipt(transactionId);

		return new ATMReceipt(bankReceipt.getTransactionId(), bankReceipt.getAmount());
	}

	@Override
	public long checkBalance()
	{
		if (checkBalanceCallCounter < 1)
		{
			checkBalanceCallCounter++;

			return bank.getBalance(atmCard.getAccountHolderId());
		}
		throw new ATMException("can not get balance");
	} 

}
