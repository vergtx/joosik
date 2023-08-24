package edu.pnu.service;

import edu.pnu.domain.CodeList;

import java.util.List;

public interface CodeListService {
    List<CodeList> getBalanceSheetItems();
    List<CodeList> getIncomeStatementItems();
    List<CodeList> getCashFlowsItems();
	List<CodeList> getFinancialStatementItems(String financialStatement, String stockCode);
}
