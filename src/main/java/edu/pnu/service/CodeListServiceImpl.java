package edu.pnu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.CodeList;
import edu.pnu.persistence.CodeListRepository;

import java.util.List;

@Service
public class CodeListServiceImpl implements CodeListService {

    private final CodeListRepository codeListRepository;

    @Autowired
    public CodeListServiceImpl(CodeListRepository codeListRepository) {
        this.codeListRepository = codeListRepository;
    }

    @Override
    public List<CodeList> getBalanceSheetItems() {
        return codeListRepository.findByInfo("B");
    }

    @Override
    public List<CodeList> getIncomeStatementItems() {
        return codeListRepository.findByInfo("I");
    }

    @Override
    public List<CodeList> getCashFlowsItems() {
        return codeListRepository.findByInfo("C");
    }

    @Override
    public List<CodeList> getFinancialStatementItems(String financialStatement, String stockCode) {
        // financialStatement와 stockCode를 기반으로 해당 재무제표 종류에 해당하는 항목들을 가져옴
        return codeListRepository.findByInfoAndStockCode(financialStatement, stockCode);
    }
}
