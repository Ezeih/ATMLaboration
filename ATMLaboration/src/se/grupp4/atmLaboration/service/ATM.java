package se.grupp4.atmLaboration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.grupp4.atmLaboration.exception.ATMException;
import se.grupp4.atmLaboration.exception.ATMSecurityException;
import se.grupp4.atmLaboration.model.ATMCard;

public final class ATM
{
	private final Map<String, Bank> banks;

	public ATM(List<Bank> banks)
	{
		if (  banks == null || banks.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		this.banks = new HashMap<>();
		for (Bank bank : banks)
		{
			this.banks.put(bank.getBankId(), bank);
		}
	}

	public ATMSession verifyPin(int pin, ATMCard card)
	{
		if (card.verifyPin(pin))
		{
			return new ATMSessionImpl(card, getBank(card));
		}
		throw new ATMSecurityException("Could not verifie pin code");
	}

	private Bank getBank(ATMCard card)
	{
		if (banks.containsKey(card.getBankId()))
		{
			return banks.get(card.getBankId());
		}
		throw new ATMException("Could not connect to bank");
	}
}