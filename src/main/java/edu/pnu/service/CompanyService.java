package edu.pnu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.pnu.domain.BalanceSheet;
import edu.pnu.domain.CodeList;
import edu.pnu.domain.Company;

@Service
public interface CompanyService {
	
    String getStockCodeByCompanyName(String companyName);

	List<Company> getAllCompany();

	Company getCompanyByStockCode(String stockCode);

	List<CodeList> getFinancialStatementItems(String statementType, String stockCode);
    
    
}