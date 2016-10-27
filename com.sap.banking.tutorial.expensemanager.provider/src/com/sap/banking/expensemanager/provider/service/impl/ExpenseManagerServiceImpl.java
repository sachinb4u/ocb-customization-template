package com.sap.banking.expensemanager.provider.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sap.banking.expensemanager.api.Transaction;
import com.sap.banking.expensemanager.api.User;
import com.sap.banking.expensemanager.api.beans.Expense;
import com.sap.banking.expensemanager.api.beans.Income;
import com.sap.banking.expensemanager.provider.service.ExpenseManagerService;

public class ExpenseManagerServiceImpl implements ExpenseManagerService {

	private Map<User, Ledger> userLedgers = new ConcurrentHashMap<>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public void addExpense(User user, Expense expense) {
		try {
			Ledger ledger = getLedger(user);
			lock.writeLock().lock();
			ledger.add(expense.getTransactionDate(), expense);
			ledger.balance = ledger.balance.subtract(expense.getAmount());

		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void addIncome(User user, Income income) {
		try {
			Ledger ledger = getLedger(user);
			lock.writeLock().lock();

			ledger.add(income.getTransactionDate(), income);
			ledger.balance = ledger.balance.add(income.getAmount());
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Collection<Transaction> getStatement(User user) {
		Ledger ledger = getLedger(user);
		try {
			lock.readLock().lock();
			return ledger.getTransactions();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public BigDecimal getCurrentBalance(User user) {
		Ledger ledger = getLedger(user);
		try {
			lock.readLock().lock();
			return ledger.balance;
		} finally {
			lock.readLock().unlock();
		}
	}

	private Ledger getLedger(User user) {
		if (userLedgers.containsKey(user)) {
			return userLedgers.get(user);
		}

		Ledger ledger = new Ledger();
		userLedgers.put(user, ledger);
		return ledger;
	}

	private static class Ledger {
		Map<Date, Transaction> entries = new TreeMap<>();
		BigDecimal balance = BigDecimal.ZERO;

		void add(Date date, Transaction transaction) {
			entries.put(date, transaction);
		}

		Collection<Transaction> getTransactions() {
			return entries.values();
		}
	}
}
