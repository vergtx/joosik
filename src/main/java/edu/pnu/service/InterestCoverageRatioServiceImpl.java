package edu.pnu.service;

import edu.pnu.domain.IncomeStatement;
import edu.pnu.persistence.IncomeStatementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InterestCoverageRatioServiceImpl implements InterestCoverageRatioService {

    private final IncomeStatementRepository incomeStatementRepository;

    @Autowired
    public InterestCoverageRatioServiceImpl(IncomeStatementRepository incomeStatementRepository) {
        this.incomeStatementRepository = incomeStatementRepository;
    }

    @Override
    public Map<Integer, Double> calculateInterestCoverageRatioByStockCode(String stockCode) {
        Map<Integer, Double> interestCoverageRatioByYear = new HashMap<>();

        for (int year = 2020; year <= 2022; year++) {
            double operatingIncome = getOperatingIncomeByYear(stockCode, year);
            double financeCosts = getFinanceCostsByYear(stockCode, year);

            if (financeCosts != 0) {
                double interestCoverageRatio = operatingIncome / financeCosts;
                interestCoverageRatioByYear.put(year, interestCoverageRatio);
            } else {
                interestCoverageRatioByYear.put(year, 0.0);
            }
        }

        return interestCoverageRatioByYear;
    }

    private double getOperatingIncomeByYear(String stockCode, int year) {
        IncomeStatement incomeStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "dart_OperatingIncomeLoss");
        if (incomeStatement != null) {
            switch (year) {
                case 2020:
                    return incomeStatement.getY2020();
                case 2021:
                    return incomeStatement.getY2021();
                case 2022:
                    return incomeStatement.getY2022();
                default:
                    return 0.0;
            }
        }
        return 0.0;
    }

    private double getFinanceCostsByYear(String stockCode, int year) {
        IncomeStatement incomeStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_FinanceCosts");
        if (incomeStatement != null) {
            switch (year) {
                case 2020:
                    return incomeStatement.getY2020();
                case 2021:
                    return incomeStatement.getY2021();
                case 2022:
                    return incomeStatement.getY2022();
                default:
                    return 0.0;
            }
        }
        return 0.0;
    }
}
