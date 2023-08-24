package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.CashFlowStatement;

public interface CashFlowStatementRepository extends JpaRepository<CashFlowStatement, Integer> {

	CashFlowStatement findByStockCode(String stockCode);

	CashFlowStatement findByStockCodeAndItemCode(String stockCode, String itemCode);

}
