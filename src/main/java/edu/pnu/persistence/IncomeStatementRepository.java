package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.IncomeStatement;

public interface IncomeStatementRepository extends JpaRepository<IncomeStatement, Integer> {

	IncomeStatement findByStockCode(String stockCode);

	IncomeStatement findByStockCodeAndItemCode(String stockCode, String itemCode);

	List<IncomeStatement> findByItemCode(String string);
	
   
}