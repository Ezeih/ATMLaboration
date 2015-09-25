package se.grupp4.atmLaboration.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.grupp4.atmLaboration.exception.ATMException;
import se.grupp4.atmLaboration.exception.ATMSecurityException;
import se.grupp4.atmLaboration.model.ATMCard;
import se.grupp4.atmLaboration.model.ATMReceipt;
import se.grupp4.atmLaboration.model.BankReceipt;
import se.grupp4.atmLaboration.service.ATM;
import se.grupp4.atmLaboration.service.ATMSession;
import se.grupp4.atmLaboration.service.Bank;

@RunWith(MockitoJUnitRunner.class)
public class ATMTest
{
	@Mock
	private Bank bank;

	private final String bankId = "1999";
	private final long balance = 300;
	private long checkedBalance = 0;
	private final String accountHolderId = "12345-1999";
	private final int pin = 1234;
	private long transactionId = 191919;
	private final int amount = 200;
	private final Date date = new Date();
	private final List<Bank> banks = new ArrayList<>();

	private final BankReceipt bankReceipt = new BankReceipt(bankId, transactionId, amount, date);
	private final ATMCard atmCard = new ATMCard(accountHolderId, bankId, pin);
	private ATM atm;
	private ATMSession atmSession;

	@Before
	public void setUp() throws Exception
	{
		when(bank.getBankId()).thenReturn(bankId);
		when(bank.getBalance(accountHolderId)).thenReturn(balance);
		when(bank.withdrawAmount(amount)).thenReturn(balance - amount);
		when(bank.requestReceipt(transactionId)).thenReturn(bankReceipt);

		banks.add(bank);
		atm = new ATM(banks);
		atmSession = atm.verifyPin(pin, atmCard);
	}

	@After
	public void tearDown()
	{
		reset(bank);
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowATMExceptionIfAmountIsNotEven()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("can not withdraw money");

		atmSession.withdrawAmount(110);
	}

	@Test
	public void shouldThrowATMExceptionIfAmountIsLessThan100()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("can not withdraw money");

		atmSession.withdrawAmount(50);
	}

	@Test
	public void shouldThrowATMExceptionIfAmountIsMoreThan10000()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("can not withdraw money");

		atmSession.withdrawAmount(11000);
	}

	@Test
	public void shouldThrowATMSecurityExceptionIfPinIsInvalid()
	{
		exception.expect(ATMSecurityException.class);
		exception.expectMessage("Could not verifie pin code");

		int invalidPin = 1235;

		atm.verifyPin(invalidPin, atmCard);

	}

	@Test
	public void shouldThrowATMExceptionIfBankCardCanNotBeUsedWithThisATM()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("Could not connect to bank");

		ATMCard atmInvalidCard = new ATMCard(accountHolderId, "1500", pin);

		atm.verifyPin(pin, atmInvalidCard);

	}

	@Test
	public void shouldThrowATMExceptionInCaseOfCallingCheckBalanceMethodeMoreAnOneTime()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("can not get balance");

		checkedBalance = atmSession.checkBalance();
		atmSession.checkBalance();
	}

	@Test
	public void shouldThrowATMExceptionInCaseOfCallingWithdrawAmountMethodeMoreAnOneTime()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("can not withdraw money");

		checkedBalance = atmSession.withdrawAmount(amount);
		atmSession.withdrawAmount(amount);
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionIfListOfBanksInATMIsNotNewList()
	{
		exception.expect(IllegalArgumentException.class);

		new ATM(new ArrayList<Bank>());
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionIfListOfBanksInATMIsNotNullList()
	{
		exception.expect(IllegalArgumentException.class);

		List<Bank> nullBanks = new ArrayList<Bank>();
		nullBanks.clear();

		new ATM(nullBanks);
	}

	@Test
	public void shouldThrowATMExceptionIfAmountIsHigherAnBalance()
	{
		exception.expect(ATMException.class);
		exception.expectMessage("can not withdraw money");

		atmSession.withdrawAmount(400);
	}

	@Test
	public void shouldCheckBalance()
	{
		checkedBalance = atmSession.checkBalance();

		assertEquals(checkedBalance, bank.getBalance(atmCard.getAccountHolderId()));

		verify(bank).getBankId();
		verify(bank, times(2)).getBalance(atmCard.getAccountHolderId());
	}

	@Test
	public void shouldGetRequestReceipt()
	{
		ATMReceipt atmReceipt = atmSession.requestReceipt(transactionId);

		assertEquals(atmReceipt.getTransactionId(), bankReceipt.getTransactionId());
		assertEquals(atmReceipt.getAmount(), bankReceipt.getAmount());
		assertTrue((atmReceipt.getDate().getTime()) - (bankReceipt.getDate().getTime()) < 1000);

		verify(bank).requestReceipt(transactionId);
	}

	@Test
	public void shouldWithdrawAmount()
	{
		checkedBalance = atmSession.withdrawAmount(amount);

		assertEquals(checkedBalance, (balance - amount));

		verify(bank).withdrawAmount(amount);
		verify(bank, times(1)).getBalance(atmCard.getAccountHolderId());
	}
}
