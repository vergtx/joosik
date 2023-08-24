package edu.pnu.service;

import java.util.Map;

public interface ReceivablesTurnoverRatioService {
    Map<Integer, Double> calculateReceivablesTurnoverRatioByStockCode(String stockCode);
}