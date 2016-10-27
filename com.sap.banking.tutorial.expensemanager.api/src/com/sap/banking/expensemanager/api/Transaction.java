package com.sap.banking.expensemanager.api;

import java.math.BigDecimal;
import java.util.Date;

import com.sap.banking.expensemanager.api.beans.TransactionType;

public interface Transaction {

	 Date getTransactionDate();
	
	 BigDecimal getAmount();
	 
	 Category getCategory();
	 
	 TransactionType getTransactionType();
}
