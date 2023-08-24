package edu.pnu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.BalanceSheet;
import edu.pnu.domain.IncomeStatement;
import edu.pnu.persistence.BalanceSheetRepository;
import edu.pnu.persistence.IncomeStatementRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class ROACalculationServiceImpl implements ROACalculationService {

    private final BalanceSheetRepository balanceSheetRepository;
    private final IncomeStatementRepository incomeStatementRepository;

    @Autowired
    public ROACalculationServiceImpl(BalanceSheetRepository balanceSheetRepository, IncomeStatementRepository incomeStatementRepository) {
        this.balanceSheetRepository = balanceSheetRepository;
        this.incomeStatementRepository = incomeStatementRepository;
    }

    @Override
    public Map<Integer, Double> calculateROAByStockCode(String stockCode) {
        Map<Integer, Double> roaByYear = new HashMap<>();
        for (int year = 2020; year <= 2022; year++) {
            double roa = calculateROAByStockCodeAndYear(stockCode, year);
            roaByYear.put(year, roa);
        }
        return roaByYear;
    }

    private double calculateROAByStockCodeAndYear(String stockCode, int year) {
        BalanceSheet balanceSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_Assets");
        long totalAssets = getBalanceByYear(balanceSheet, year);

        IncomeStatement incomeStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_ProfitLoss");
        long netProfit = getProfitByYear(incomeStatement, year);

        if (totalAssets == 0) {
            return 0.0;
        }

        return (double) netProfit / totalAssets;
    }

    private long getBalanceByYear(BalanceSheet balanceSheet, int year) {
        if (balanceSheet != null) {
            switch (year) {
                case 2020:
                    return balanceSheet.getY2020();
                case 2021:
                    return balanceSheet.getY2021();
                case 2022:
                    return balanceSheet.getY2022();
                // 다른 년도를 사용하고 싶다면 해당 case를 추가하면 됩니다.
                default:
                    return 0; // 해당 년도의 데이터가 없으면 0을 반환합니다.
            }
        }
        return 0; // 재무상태표가 없는 경우 0을 반환합니다.
    }

    private long getProfitByYear(IncomeStatement incomeStatement, int year) {
        if (incomeStatement != null) {
            switch (year) {
                case 2020:
                    return incomeStatement.getY2020();
                case 2021:
                    return incomeStatement.getY2021();
                case 2022:
                    return incomeStatement.getY2022();
                // 다른 년도를 사용하고 싶다면 해당 case를 추가하면 됩니다.
                default:
                    return 0; // 해당 년도의 데이터가 없으면 0을 반환합니다.
            }
        }
        return 0; // 손익계산서가 없는 경우 0을 반환합니다.
    }

}
