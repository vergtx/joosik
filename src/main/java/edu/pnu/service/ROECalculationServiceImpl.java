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
public class ROECalculationServiceImpl implements ROECalculationService {

    private final BalanceSheetRepository balanceSheetRepository;
    private final IncomeStatementRepository incomeStatementRepository;

    @Autowired
    public ROECalculationServiceImpl(BalanceSheetRepository balanceSheetRepository,
                                     IncomeStatementRepository incomeStatementRepository) {
        this.balanceSheetRepository = balanceSheetRepository;
        this.incomeStatementRepository = incomeStatementRepository;
    }

    @Override
    public Map<Integer, Double> calculateROEByStockCode(String stockCode) {
        Map<Integer, Double> roeByYear = new HashMap<>();
        for (int year = 2020; year <= 2022; year++) {
            double roe = calculateROEByYear(stockCode, year);
            roeByYear.put(year, roe);
        }
        return roeByYear;
    }

    // 특정 년도의 ROE 계산하는 메서드
    private double calculateROEByYear(String stockCode, int year) {
        // 해당 년도의 이익 계산서 정보를 가져옵니다.
        IncomeStatement incomeStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_ProfitLoss");
        if (incomeStatement == null) {
            // 이익 계산서 정보가 없으면 ROE를 계산할 수 없으므로 0.0을 반환합니다.
            return 0.0;
        }

        // 해당 년도의 재무상태표 정보를 가져옵니다.
        BalanceSheet balanceSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_Equity");
        if (balanceSheet == null) {
            // 재무상태표 정보가 없으면 ROE를 계산할 수 없으므로 0.0을 반환합니다.
            return 0.0;
        }

        // 이익 계산서에서 해당 년도의 순이익을 가져옵니다.
        long profitLoss = getProfitLossByYear(incomeStatement, year);

        // 재무상태표에서 해당 년도의 자기자본을 가져옵니다.
        long equity = getEquityByYear(balanceSheet, year);

        if (equity == 0) {
            // 자기자본이 0인 경우 나누기 연산을 피하기 위해 0.0을 반환합니다.
            return 0.0;
        }

        // ROE를 계산하여 반환합니다.
        return (double) profitLoss / equity;
    }

    // 이익 계산서에서 해당 년도의 순이익을 가져오는 메서드
    private long getProfitLossByYear(IncomeStatement incomeStatement, int year) {
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
        return 0; // 이익 계산서가 없는 경우 0을 반환합니다.
    }

    // 재무상태표에서 해당 년도의 자기자본을 가져오는 메서드
    private long getEquityByYear(BalanceSheet balanceSheet, int year) {
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
}
