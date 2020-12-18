package com.gepardec.cheetunit.core;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

public class TransactionSupport {
	
	@Inject
	private UserTransaction tx;

	public void beginTx() {
		try {
			tx.begin();
		} catch (Exception e){
			throw new CheetUnitException("Exception while starting a transaction", e);
		}
	}

	public void commitTx() {
		try {
			tx.commit();
		} catch (Exception e){
			throw new CheetUnitException("Exception while committing a transaction", e);
		}
	}

	public void rollbackTx() {
		try {
			tx.rollback();
		} catch (Exception e){
			throw new CheetUnitException("Exception while performing transaction rollback", e);
		}
	}

}
