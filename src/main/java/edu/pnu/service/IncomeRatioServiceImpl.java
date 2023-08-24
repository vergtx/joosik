package edu.pnu.service;

import edu.pnu.domain.IncomeStatement;
import edu.pnu.persistence.IncomeStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomeRatioServiceImpl implements IncomeRatioService {

    private final IncomeStatementRepository incomeStatementRepository;

    @Autowired
    public IncomeRatioServiceImpl(IncomeStatementRepository incomeStatementRepository) {
        this.incomeStatementRepository = incomeStatementRepository;
    }

    @Override
    public double getIncomeRatioByStockCodeAndYear(String stockCode, int year) {
        // 손익계산서에서 해당 년도의 "ifrs-full_ProfitLoss" 항목을 찾아 순이익을 구합니다.
        IncomeStatement profitLossStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_ProfitLoss");
        long netIncome = getIncomeByYear(profitLossStatement, year);

        // 손익계산서에서 해당 년도의 "ifrs-full_Revenue" 항목을 찾아 매출액을 구합니다.
        IncomeStatement revenueStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, "ifrs-full_Revenue");
        long revenue = getIncomeByYear(revenueStatement, year);

        if (revenue == 0) {
            // 매출액이 0인 경우 나누기 연산을 피하기 위해 0.0을 반환합니다.
            return 0.0;
        }

        // 매출액 순이익율을 계산하여 반환합니다.
        return (double) netIncome / revenue;
    }

    // 해당 년도의 손익계산서에서 데이터를 찾아 반환하는 메서드
    private long getIncomeByYear(IncomeStatement incomeStatement, int year) {
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
