package com.sap.banking.expensemanager.provider.service;

import java.math.BigDecimal;
import java.util.Collection;

import com.sap.banking.expensemanager.api.Transaction;
import com.sap.banking.expensemanager.api.User;
import com.sap.banking.expensemanager.api.beans.Expense;
import com.sap.banking.expensemanager.api.beans.Income;

public interface ExpenseManagerService {

	void addExpense(User user, Expense expense);

	void addIncome(User user, Income income);

	Collection<Transaction> getStatement(User user);

	BigDecimal getCurrentBalance(User user);

}
