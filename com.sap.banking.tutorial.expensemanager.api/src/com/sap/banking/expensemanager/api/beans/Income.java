package com.sap.banking.expensemanager.api.beans;

import java.math.BigDecimal;
import java.util.Date;

import com.sap.banking.expensemanager.api.Category;
import com.sap.banking.expensemanager.api.Transaction;

public class Income implements Transaction {

	private Date transactionDate;
	private BigDecimal amount;
	private IncomeCategory category;
	
	@Override
	public Date getTransactionDate() {
		return transactionDate;
	}

	@Override
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public TransactionType getTransactionType() {
		return TransactionType.CREDIT;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setCategory(IncomeCategory category) {
		this.category = category;
	}

}
