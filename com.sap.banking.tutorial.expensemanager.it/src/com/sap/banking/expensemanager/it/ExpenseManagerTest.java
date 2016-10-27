package com.sap.banking.expensemanager.it;

import static com.sap.banking.expensemanager.api.beans.ExpenseCategory.Entertainment;
import static com.sap.banking.expensemanager.api.beans.ExpenseCategory.Food;
import static com.sap.banking.expensemanager.api.beans.ExpenseCategory.Insurance;
import static com.sap.banking.expensemanager.api.beans.IncomeCategory.Pensions;
import static com.sap.banking.expensemanager.api.beans.IncomeCategory.Salary;
import static java.math.BigDecimal.ZERO;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.sap.banking.expensemanager.api.ExpenseManager;
import com.sap.banking.expensemanager.api.Transaction;
import com.sap.banking.expensemanager.api.User;
import com.sap.banking.expensemanager.api.beans.Expense;
import com.sap.banking.expensemanager.api.beans.Income;
import com.sap.banking.expensemanager.api.beans.UserImpl;

public class ExpenseManagerTest {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private ExpenseManager getExpenseManager() {
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

		ServiceTracker<ExpenseManager, ExpenseManager> expenseManagerServiceTracker = new ServiceTracker<>(context, ExpenseManager.class, null);

		try {
			expenseManagerServiceTracker.open();

			ExpenseManager expenseManager = expenseManagerServiceTracker.waitForService(SECONDS.toMillis(2));

			expenseManagerServiceTracker.close();
			Assert.assertNotNull("ExpenseManager service is not available", expenseManager);
			return expenseManager;
		} catch (InterruptedException e) {
			fail("ExpenseManager service not available : " + e.getMessage());
			return null;
		}

	}

	@Test
	public void test_initial_state() {
		ExpenseManager expenseManager = getExpenseManager();
		
		User user = getUser("User0");

		Collection<Transaction> statement = expenseManager.getStatement(user);

		assertNotNull("Statement should not be null", statement);
		assertTrue("Statement should be empty", statement.isEmpty());
		assertEquals("Current Balance should be 0", ZERO, expenseManager.getCurrentBalance(user));
	}

	@Test
	public void test_AddIncome() {
		ExpenseManager expenseManager = getExpenseManager();

		User user = getUser("User1");
		
		Income income = new Income();
		income.setAmount(BigDecimal.valueOf(3456));
		income.setCategory(Salary);
		income.setTransactionDate(new Date());

		expenseManager.addIncome(user, income);

		Collection<Transaction> statement = expenseManager.getStatement(user);
		assertNotNull("Statement should not be null", statement);
		assertFalse("Statement should not be empty", statement.isEmpty());
		assertEquals("1 Expense entry should be there", 1, statement.size());

		Transaction resultIncome = (Transaction) statement.toArray()[0];
		assertEquals("Transaction Amount should be 10", BigDecimal.valueOf(3456), resultIncome.getAmount());
		assertEquals("Current Balance should be -10", BigDecimal.valueOf(3456), expenseManager.getCurrentBalance(user));
		assertEquals("Income category should be Salary", Salary, resultIncome.getCategory());
	}

	@Test
	public void test_AddExpense() {
		ExpenseManager expenseManager = getExpenseManager();

		User user = getUser("User2");
		
		Expense expense = new Expense();
		expense.setAmount(BigDecimal.valueOf(10));
		expense.setCategory(Insurance);
		expense.setTransactionDate(new Date());

		expenseManager.addExpense(user, expense);

		Collection<Transaction> statement = expenseManager.getStatement(user);
		assertNotNull("Statement should not be null", statement);
		assertFalse("Statement should not be empty", statement.isEmpty());
		assertEquals("1 Expense entry should be there", 1, statement.size());

		Transaction resultExpense = (Transaction) statement.toArray()[0];
		assertEquals("Transaction Amount should be 10", BigDecimal.valueOf(10), resultExpense.getAmount());
		assertEquals("Current Balance should be -10", BigDecimal.valueOf(-10), expenseManager.getCurrentBalance(user));
		assertEquals("Expense Category should be Household", Insurance, resultExpense.getCategory());
	}
	
	@Test
	public void test_multiple_transactions() throws ParseException{
		ExpenseManager expenseManager = getExpenseManager();

		User user = getUser("User3");
		
		Expense expense = new Expense();
		expense.setAmount(BigDecimal.valueOf(50));
		expense.setCategory(Food);
		expense.setTransactionDate(dateFormat.parse("2016-10-25"));
		
		expenseManager.addExpense(user, expense);
		
		Expense expense2 = new Expense();
		expense2.setAmount(BigDecimal.valueOf(23));
		expense2.setCategory(Entertainment);
		expense2.setTransactionDate(dateFormat.parse("2016-08-17"));

		expenseManager.addExpense(user, expense2);
		
		Income income = new Income();
		income.setAmount(BigDecimal.valueOf(1000));
		income.setCategory(Pensions);
		income.setTransactionDate(dateFormat.parse("2016-07-01"));
		
		expenseManager.addIncome(user, income);
		
		
		assertEquals("CurrentBalance should be 927", BigDecimal.valueOf(927), expenseManager.getCurrentBalance(user));
	}
	
	private User getUser(String userId){
		UserImpl user = new UserImpl();
		user.setUserId(userId);
		
		return user;
	}

}
