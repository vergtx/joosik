package edu.pnu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.BalanceSheet;
import edu.pnu.persistence.BalanceSheetRepository;

@Service
public class LiquidityRatioServiceImpl implements LiquidityRatioService {

    private final BalanceSheetRepository balanceSheetRepository;

    @Autowired
    public LiquidityRatioServiceImpl(BalanceSheetRepository balanceSheetRepository) {
        this.balanceSheetRepository = balanceSheetRepository;
    }

    @Override
    public double getLiquidityRatioByStockCodeAndYear(String stockCode, int year) {
        // 재무상태표에서 해당 년도의 "ifrs-full_CurrentAssets" 항목을 찾아 유동자산을 구합니다.
        BalanceSheet currentAssetsSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_CurrentAssets");
        long currentAssets = getBalanceByYear(currentAssetsSheet, year);

        // 재무상태표에서 해당 년도의 "ifrs-full_CurrentLiabilities" 항목을 찾아 유동부채를 구합니다.
        BalanceSheet currentLiabilitiesSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_CurrentLiabilities");
        long currentLiabilities = getBalanceByYear(currentLiabilitiesSheet, year);

        if (currentLiabilities == 0) {
            // 유동부채가 0인 경우 나누기 연산을 피하기 위해 0.0을 반환합니다.
            return 0.0;
        }

        // 유동비율을 계산하여 반환합니다.
        return (double) currentAssets / currentLiabilities;
    }

    // 해당 년도의 재무상태표에서 자산 또는 부채를 찾아 반환하는 메서드
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
}