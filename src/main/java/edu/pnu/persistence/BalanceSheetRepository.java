package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.BalanceSheet;

public interface BalanceSheetRepository extends JpaRepository<BalanceSheet, Integer> {

	BalanceSheet findByStockCodeAndItemCode(String stockCode, String itemCode);
	
	
}
