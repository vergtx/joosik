package edu.pnu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.BalanceSheet;
import edu.pnu.persistence.BalanceSheetRepository;

@Service
public class CurrentRatioServiceImpl implements CurrentRatioService {

    private final BalanceSheetRepository balanceSheetRepository;

    @Autowired
    public CurrentRatioServiceImpl(BalanceSheetRepository balanceSheetRepository) {
        this.balanceSheetRepository = balanceSheetRepository;
    }

    @Override
    public double calculateCurrentRatio(String stockCode) {
        BalanceSheet currentAssets = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_CurrentAssets");
        BalanceSheet currentLiabilities = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_CurrentLiabilities");

        double currentAssetsValue = currentAssets.getY2022();
        double currentLiabilitiesValue = currentLiabilities.getY2022();

        return currentAssetsValue / currentLiabilitiesValue;
    }
}
