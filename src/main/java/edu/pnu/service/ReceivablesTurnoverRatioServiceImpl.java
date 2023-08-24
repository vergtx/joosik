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
public class ReceivablesTurnoverRatioServiceImpl implements ReceivablesTurnoverRatioService {

    private final BalanceSheetRepository balanceSheetRepository;
    private final IncomeStatementRepository incomeStatementRepository;

    @Autowired
    public ReceivablesTurnoverRatioServiceImpl(BalanceSheetRepository balanceSheetRepository, IncomeStatementRepository incomeStatementRepository) {
        this.balanceSheetRepository = balanceSheetRepository;
        this.incomeStatementRepository = incomeStatementRepository;
    }

    private double getRevenueByYear(String stockCode, int year) {
        IncomeStatement incomeStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_Revenue");
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
                    return 0.0; // 해당 년도의 데이터가 없으면 0.0을 반환합니다.
            }
        }
        return 0.0; // 손익계산서가 없는 경우 0.0을 반환합니다.
    }

    private long getShortTermTradeReceivableByYear(String stockCode, int year) {
        BalanceSheet balanceSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "dart_ShortTermTradeReceivable");
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
        return 0; // 재무상태표에 매출채권 데이터가 없는 경우 0을 반환합니다.
    }

    @Override
    public Map<Integer, Double> calculateReceivablesTurnoverRatioByStockCode(String stockCode) {
        Map<Integer, Double> receivablesTurnoverRatioByYear = new HashMap<>();
        for (int year = 2020; year <= 2022; year++) {
            double revenue = getRevenueByYear(stockCode, year);
            long shortTermTradeReceivable = getShortTermTradeReceivableByYear(stockCode, year);

            // 매출채권 회전율을 계산합니다.
            if (revenue != 0 && shortTermTradeReceivable != 0) {
                double receivablesTurnoverRatio = revenue / shortTermTradeReceivable;
                receivablesTurnoverRatioByYear.put(year, receivablesTurnoverRatio);
            } else {
                receivablesTurnoverRatioByYear.put(year, 0.0); // 매출액이나 매출채권이 0인 경우 0.0을 저장합니다.
            }
        }
        return receivablesTurnoverRatioByYear;
    }
}
