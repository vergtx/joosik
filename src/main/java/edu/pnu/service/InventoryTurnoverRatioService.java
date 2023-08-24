package edu.pnu.service;

import java.util.Map;

public interface InventoryTurnoverRatioService {
    Map<Integer, Double> calculateInventoryTurnoverRatioByStockCode(String stockCode);
}
