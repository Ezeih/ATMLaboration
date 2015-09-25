package se.grupp4.atmLaboration.service;

import se.grupp4.atmLaboration.model.ATMCard;

public abstract class AbstractATMSession implements ATMSession
{
	protected final ATMCard atmCard;
	protected final Bank bank;

	public AbstractATMSession(ATMCard atmCard, Bank bank)
	{
		this.atmCard = atmCard;
		this.bank = bank;
	}
}
