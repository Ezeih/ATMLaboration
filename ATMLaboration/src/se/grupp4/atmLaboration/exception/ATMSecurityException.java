package se.grupp4.atmLaboration.exception;

public class ATMSecurityException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ATMSecurityException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ATMSecurityException(String message)
	{
		super(message);
	}

}
