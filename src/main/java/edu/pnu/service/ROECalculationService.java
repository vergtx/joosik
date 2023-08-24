package edu.pnu.service;

import java.util.Map;

public interface ROECalculationService {
    Map<Integer, Double> calculateROEByStockCode(String stockCode);
}