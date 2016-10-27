/**
 * 
 */
package com.sap.banking.expensemanager.api;

import java.math.BigDecimal;
import java.util.Collection;

import com.sap.banking.expensemanager.api.beans.Expense;
import com.sap.banking.expensemanager.api.beans.Income;

/**
 * @author I313873
 *
 */
public interface ExpenseManager {

	void addExpense(User user, Expense expense);

	void addIncome(User user, Income income);

	Collection<Transaction> getStatement(User user);

	BigDecimal getCurrentBalance(User user);
}
