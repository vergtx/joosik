package edu.pnu.service;

public interface DebtRatioService {
	
    double getDebtRatioByStockCodeAndYear(String stockCode, int year);
}
