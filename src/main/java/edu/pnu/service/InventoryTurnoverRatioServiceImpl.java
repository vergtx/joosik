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
public class InventoryTurnoverRatioServiceImpl implements InventoryTurnoverRatioService {

    private final BalanceSheetRepository balanceSheetRepository;
    private final IncomeStatementRepository incomeStatementRepository;

    @Autowired
    public InventoryTurnoverRatioServiceImpl(BalanceSheetRepository balanceSheetRepository, IncomeStatementRepository incomeStatementRepository) {
        this.balanceSheetRepository = balanceSheetRepository;
        this.incomeStatementRepository = incomeStatementRepository;
    }

    private double getCostOfSalesByYear(String stockCode, int year) {
        IncomeStatement incomeStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_CostOfSales");
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

    private long getInventoryByYear(String stockCode, int year) {
        BalanceSheet balanceSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_Inventories");
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
        return 0; // 재무상태표에 재고자산 데이터가 없는 경우 0을 반환합니다.
    }

    @Override
    public Map<Integer, Double> calculateInventoryTurnoverRatioByStockCode(String stockCode) {
        Map<Integer, Double> inventoryTurnoverRatioByYear = new HashMap<>();
        for (int year = 2020; year <= 2022; year++) {
            double costOfSales = getCostOfSalesByYear(stockCode, year);
            long inventory = getInventoryByYear(stockCode, year);

            // 재고자산 회전율을 계산합니다.
            if (costOfSales != 0 && inventory != 0) {
                double inventoryTurnoverRatio = costOfSales / inventory;
                inventoryTurnoverRatioByYear.put(year, inventoryTurnoverRatio);
            } else {
                inventoryTurnoverRatioByYear.put(year, 0.0); // 매출원가나 재고자산이 0인 경우 0.0을 저장합니다.
            }
        }
        return inventoryTurnoverRatioByYear;
    }
}
