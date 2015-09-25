package se.grupp4.atmLaboration.exception;


public class ATMException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ATMException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ATMException(String message)
	{
		super(message);
	}

}
