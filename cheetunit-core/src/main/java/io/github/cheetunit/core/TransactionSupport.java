/*
 * Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.cheetunit.core;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

class TransactionSupport {
	
	@Inject
	private UserTransaction tx;

	void beginTx() {
		try {
			tx.begin();
		} catch (Exception e){
			throw new CheetUnitServerException("Exception while starting a transaction", e);
		}
	}

	void commitTx() {
		try {
			tx.commit();
		} catch (Exception e){
			throw new CheetUnitServerException("Exception while committing a transaction", e);
		}
	}

	void rollbackTx() {
		try {
			tx.rollback();
		} catch (Exception e){
			throw new CheetUnitServerException("Exception while performing transaction rollback", e);
		}
	}

}
