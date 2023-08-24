package edu.pnu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.pnu.domain.BalanceSheet;
import edu.pnu.domain.CodeList;
import edu.pnu.domain.Company;
import edu.pnu.persistence.BalanceSheetRepository;
import edu.pnu.persistence.CodeListRepository;
import edu.pnu.persistence.CompanyRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CodeListService codeListService;
    private final CodeListRepository codeListRepository;
    private final BalanceSheetRepository balanceSheetRepository;


    @Override
    public String getStockCodeByCompanyName(String companyName) {
        Company company = companyRepository.findByCompanyName(companyName);
        if (company != null) {
            return company.getStockCode();
        } else {
            return null; // 해당하는 회사 이름의 데이터가 없는 경우 null 반환
        }
    }

	@Override
	public List<Company> getAllCompany() {
		return companyRepository.findAll();
		
	}

	@Override
	public Company getCompanyByStockCode(String stockCode) {
		return companyRepository.findById(stockCode).get();
	}

//	@Override
//	public List<BalanceSheet> getFinancialStatementByStockCode(String stockCode) {
//		return balanceSheetRepository.findByStockCode(stockCode);
//		
//	}
    
   

    @Override
    public List<CodeList> getFinancialStatementItems(String statementType, String stockCode) {
        return codeListRepository.findByInfoAndStockCode(statementType, stockCode);
    }



	
}
