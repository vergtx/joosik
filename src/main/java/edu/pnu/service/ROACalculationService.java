package edu.pnu.service;

import java.util.Map;

public interface ROACalculationService {
    Map<Integer, Double> calculateROAByStockCode(String stockCode);
}
