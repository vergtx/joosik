package edu.pnu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.pnu.domain.IncomeStatement;
import edu.pnu.domain.BalanceSheet;
import edu.pnu.persistence.IncomeStatementRepository;
import edu.pnu.persistence.BalanceSheetRepository;
import java.util.HashMap;
import java.util.Map;

@Service
public class TotalAssetTurnoverServiceImpl implements TotalAssetTurnoverService {

    private final IncomeStatementRepository incomeStatementRepository;
    private final BalanceSheetRepository balanceSheetRepository;

    @Autowired
    public TotalAssetTurnoverServiceImpl(IncomeStatementRepository incomeStatementRepository,
                                         BalanceSheetRepository balanceSheetRepository) {
        this.incomeStatementRepository = incomeStatementRepository;
        this.balanceSheetRepository = balanceSheetRepository;
    }

    @Override
    public Map<Integer, Double> calculateTotalAssetTurnoverByStockCode(String stockCode) {
        Map<Integer, Double> totalAssetTurnoverByYear = new HashMap<>();

        // 각 연도별 매출액과 총자산을 가져와서 총자산 회전율을 계산합니다.
        for (int year = 2020; year <= 2022; year++) {
            double revenue = getRevenueByYear(stockCode, year);
            long totalAssets = getTotalAssetsByYear(stockCode, year);

            if (totalAssets == 0) {
                // 총자산이 0인 경우 나누기 연산을 피하기 위해 0.0을 반환합니다.
                totalAssetTurnoverByYear.put(year, 0.0);
            } else {
                double totalAssetTurnover = revenue / totalAssets;
                totalAssetTurnoverByYear.put(year, totalAssetTurnover);
            }
        }

        return totalAssetTurnoverByYear;
    }

    // 해당 년도의 손익계산서에서 매출액을 가져오는 메서드
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

    // 해당 년도의 재무상태표에서 총자산을 가져오는 메서드
    private long getTotalAssetsByYear(String stockCode, int year) {
        BalanceSheet balanceSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_Assets");
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