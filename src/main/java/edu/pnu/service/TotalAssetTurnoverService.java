package edu.pnu.service;

import java.util.Map;

public interface TotalAssetTurnoverService {
	
    Map<Integer, Double> calculateTotalAssetTurnoverByStockCode(String stockCode);

}