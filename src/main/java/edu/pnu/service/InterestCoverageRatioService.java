package edu.pnu.service;

import java.util.Map;

public interface InterestCoverageRatioService {
    Map<Integer, Double> calculateInterestCoverageRatioByStockCode(String stockCode);
}
