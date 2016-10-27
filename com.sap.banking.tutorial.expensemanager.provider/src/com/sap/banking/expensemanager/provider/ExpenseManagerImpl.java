package com.sap.banking.expensemanager.provider;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.sap.banking.expensemanager.api.ExpenseManager;
import com.sap.banking.expensemanager.api.Transaction;
import com.sap.banking.expensemanager.api.User;
import com.sap.banking.expensemanager.api.beans.Expense;
import com.sap.banking.expensemanager.api.beans.Income;
import com.sap.banking.expensemanager.provider.service.ExpenseManagerService;

public class ExpenseManagerImpl implements ExpenseManager {

	@Autowired
	private ExpenseManagerService service;
	
	@Override
	public void addExpense(User user, Expense expense) {
		service.addExpense(user, expense);
	}

	@Override
	public void addIncome(User user, Income income) {
		service.addIncome(user, income);
	}

	@Override
	public Collection<Transaction> getStatement(User user) {
		return service.getStatement(user);
	}

	@Override
	public BigDecimal getCurrentBalance(User user) {
		return service.getCurrentBalance(user);
	}
	
	public ExpenseManagerService getService() {
		return service;
	}

	public void setService(ExpenseManagerService service) {
		this.service = service;
	}


}
