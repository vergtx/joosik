package edu.pnu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.BalanceSheet;
import edu.pnu.domain.CashFlowStatement;
import edu.pnu.domain.CodeList;
import edu.pnu.domain.Company;
import edu.pnu.domain.IncomeStatement;
import edu.pnu.persistence.BalanceSheetRepository;
import edu.pnu.persistence.CashFlowStatementRepository;
import edu.pnu.persistence.IncomeStatementRepository;
import edu.pnu.service.CodeListService;
import edu.pnu.service.CompanyService;
import edu.pnu.service.DebtRatioService;
import edu.pnu.service.IncomeRatioService;
import edu.pnu.service.InterestCoverageRatioService;
import edu.pnu.service.InventoryTurnoverRatioService;
import edu.pnu.service.LiquidityRatioService;
import edu.pnu.service.ROACalculationService;
import edu.pnu.service.ROECalculationService;
import edu.pnu.service.ReceivablesTurnoverRatioService;
import edu.pnu.service.TotalAssetTurnoverService;

@RestController
@RequestMapping("/company")
public class CompanyController {

	private final CompanyService companyService;
	private final CodeListService codeListService;
	private final BalanceSheetRepository balanceSheetRepository;
	private final CashFlowStatementRepository cashFlowStatementRepository;
	private final IncomeStatementRepository incomeStatementRepository;
	private final DebtRatioService debtRatioService;
    private final LiquidityRatioService liquidityRatioService;
    private final IncomeRatioService incomeRatioService;
    private final ROECalculationService roeCalculationService;
    private final ROACalculationService roaCalculationService;
    private final TotalAssetTurnoverService totalAssetTurnoverService;
    private final ReceivablesTurnoverRatioService receivablesTurnoverRatioService;
    private final InventoryTurnoverRatioService inventoryTurnoverRatioService;
    private final InterestCoverageRatioService interestCoverageRatioService;



	

    @Autowired
    public CompanyController(CompanyService companyService, CodeListService codeListService,
                             BalanceSheetRepository balanceSheetRepository, CashFlowStatementRepository cashFlowStatementRepository,
                             IncomeStatementRepository incomeStatementRepository,
                             DebtRatioService debtRatioService, LiquidityRatioService liquidityRatioService,
                             IncomeRatioService incomeRatioService, ROECalculationService roeCalculationService, ROACalculationService roaCalculationService,
                             TotalAssetTurnoverService totalAssetTurnoverService, ReceivablesTurnoverRatioService receivablesTurnoverRatioService,
                             InventoryTurnoverRatioService inventoryTurnoverRatioService, InterestCoverageRatioService interestCoverageRatioService) {
        this.companyService = companyService;
        this.codeListService = codeListService;
        this.balanceSheetRepository = balanceSheetRepository;
        this.cashFlowStatementRepository = cashFlowStatementRepository;
        this.incomeStatementRepository = incomeStatementRepository;
        this.debtRatioService = debtRatioService;
        this.liquidityRatioService = liquidityRatioService;
        this.incomeRatioService = incomeRatioService;
        this.roeCalculationService = roeCalculationService;
        this.roaCalculationService = roaCalculationService;
        this.totalAssetTurnoverService = totalAssetTurnoverService;
        this.receivablesTurnoverRatioService = receivablesTurnoverRatioService;
        this.inventoryTurnoverRatioService = inventoryTurnoverRatioService;
        this.interestCoverageRatioService = interestCoverageRatioService;
    }

	@GetMapping
	public List<Company> getAllCompany() {
		return companyService.getAllCompany();

	}
//
//    @GetMapping("/{companyName}")
//    public String getStockCodeByCompanyName(@PathVariable String companyName) {
//        String stockCode = companyService.getStockCodeByCompanyName(companyName);
//        if (stockCode != null) {
//            return stockCode;
//        } else {
//            return "회사 이름이 잘못되었습니다!";
//        }
//    }

	@GetMapping("/{stockCode}")
	public Company getCompanyByStockCode(@PathVariable String stockCode) {
		return companyService.getCompanyByStockCode(stockCode);

	}

	@GetMapping("/{FinancialStatement}/{stockCode}")
	public ResponseEntity<Map<String, Object>> getFinancialStatementByStockCode(
	        @PathVariable String FinancialStatement,
	        @PathVariable String stockCode
	) {
	    // 재무상태표 종류에 따라 해당하는 데이터를 조회
	    // B: 재무상태표, C: 현금흐름표, I: 손익계산서
	    String statementType;
	    if (FinancialStatement.equalsIgnoreCase("B")) {
	        statementType = "B";
	    } else if (FinancialStatement.equalsIgnoreCase("C")) {
	        statementType = "C";
	    } else if (FinancialStatement.equalsIgnoreCase("I")) {
	        statementType = "I";
	    } else {
	        return ResponseEntity.badRequest().build();
	    }

	    // 해당 재무상태표 종류에 따른 codelist 테이블의 item_code 내용 출력 
	    Map<String, Object> resultMap = new HashMap<>();
	    List<Map<String, Object>> statements = new ArrayList<>();
	    for (CodeList codeList : codeListService.getFinancialStatementItems(statementType, stockCode)) {
	        Map<String, Object> statementMap = new HashMap<>();
	        statementMap.put("itemCode", codeList.getItemCode());
	        statementMap.put("itemName", codeList.getItemName());

	        if (FinancialStatement.equalsIgnoreCase("B")) {
	            BalanceSheet balanceSheet = balanceSheetRepository.findByStockCodeAndItemCode(stockCode, codeList.getItemCode());
	            if (balanceSheet != null) {
	                statementMap.put("y2020", balanceSheet.getY2020());
	                statementMap.put("y2021", balanceSheet.getY2021());
	                statementMap.put("y2022", balanceSheet.getY2022());
	            }
	        } else if (FinancialStatement.equalsIgnoreCase("C")) {
	            CashFlowStatement cashFlowStatement = cashFlowStatementRepository.findByStockCodeAndItemCode(stockCode, codeList.getItemCode());
	            if (cashFlowStatement != null) {
	                statementMap.put("y2020", cashFlowStatement.getY2020());
	                statementMap.put("y2021", cashFlowStatement.getY2021());
	                statementMap.put("y2022", cashFlowStatement.getY2022());
	            }
	        } else if (FinancialStatement.equalsIgnoreCase("I")) {
	            IncomeStatement incomeStatement = incomeStatementRepository.findByStockCodeAndItemCode(stockCode, codeList.getItemCode());
	            if (incomeStatement != null) {
	                statementMap.put("y2020", incomeStatement.getY2020());
	                statementMap.put("y2021", incomeStatement.getY2021());
	                statementMap.put("y2022", incomeStatement.getY2022());
	            }
	        }

	        statements.add(statementMap);
	    }

	    resultMap.put("statements", statements);

	    return ResponseEntity.ok(resultMap);
	}
	
	//부채비율
	@GetMapping("/debt-ratio/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> getDebtRatioByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> debtRatioByYear = new HashMap<>();
        for (int year = 2020; year <= 2022; year++) {
            double debtRatio = debtRatioService.getDebtRatioByStockCodeAndYear(stockCode, year);
            debtRatioByYear.put(year, debtRatio);
        }
        return ResponseEntity.ok(debtRatioByYear);
    }
	
	//유동비율
	@GetMapping("/liquidity-ratio/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> getLiquidityRatioByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> liquidityRatioByYear = new HashMap<>();
        for (int year = 2020; year <= 2022; year++) {
            double liquidityRatio = liquidityRatioService.getLiquidityRatioByStockCodeAndYear(stockCode, year);
            liquidityRatioByYear.put(year, liquidityRatio);
        }
        return ResponseEntity.ok(liquidityRatioByYear);
    }
	
	//매출액 순이익율
    @GetMapping("/income-ratio/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> getIncomeRatioByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> incomeRatioByYear = new HashMap<>();
        for (int year = 2020; year <= 2022; year++) {
            double incomeRatio = incomeRatioService.getIncomeRatioByStockCodeAndYear(stockCode, year);
            incomeRatioByYear.put(year, incomeRatio);
        }
        return ResponseEntity.ok(incomeRatioByYear);
    }
    
    //ROE 총자본이익율
    @GetMapping("/roe/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> calculateROEByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> roeByYear = roeCalculationService.calculateROEByStockCode(stockCode);
        return ResponseEntity.ok(roeByYear);
    }
    
 // ROA 총자산이익율 = 
    @GetMapping("/roa/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> calculateROAByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> roaByYear = roaCalculationService.calculateROAByStockCode(stockCode);
        return ResponseEntity.ok(roaByYear);
    }
    //총자산 회전율 = 매출액 (ifrs-full_Revenue) / 총자산 (ifrs-full_Assets)
    @GetMapping("/total-asset-turnover/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> calculateTotalAssetTurnoverByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> totalAssetTurnoverByYear = totalAssetTurnoverService.calculateTotalAssetTurnoverByStockCode(stockCode);
        return ResponseEntity.ok(totalAssetTurnoverByYear);
    }

    //매출채권회전율(Receivables Turnover Ratio) = 매출액 / 평균 매출채권
    @GetMapping("/receivables-turnover-ratio/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> calculateReceivablesTurnoverRatioByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> receivablesTurnoverRatioByYear = receivablesTurnoverRatioService.calculateReceivablesTurnoverRatioByStockCode(stockCode);
        return ResponseEntity.ok(receivablesTurnoverRatioByYear);
    }
    
    //재고자산 회전율 = 매출원가 (ifrs-full_CostOfSales) / 재고자산 (ifrs-full_Inventories)
    @GetMapping("/inventory-turnover-ratio/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> calculateInventoryTurnoverRatioByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> inventoryTurnoverRatioByYear = inventoryTurnoverRatioService.calculateInventoryTurnoverRatioByStockCode(stockCode);
        return ResponseEntity.ok(inventoryTurnoverRatioByYear);
    }
    
    //이자보상비율 = 영업이익(dart_OperatingIncomeLoss) / 이자비용(ifrs-full_FinanceCosts)
    @GetMapping("/interest-coverage-ratio/{stockCode}")
    public ResponseEntity<Map<Integer, Double>> getInterestCoverageRatioByStockCode(@PathVariable String stockCode) {
        Map<Integer, Double> interestCoverageRatioByYear = interestCoverageRatioService.calculateInterestCoverageRatioByStockCode(stockCode);
        return ResponseEntity.ok(interestCoverageRatioByYear);
    }
    


}
