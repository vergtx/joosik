package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pnu.domain.CodeList;

public interface CodeListRepository extends JpaRepository<CodeList, String> {
	// 재무제표 종류를 info열에서  'B,c,i'에 해당하는 데이터를 필터링
	List<CodeList> findByInfo(String info);
	List<CodeList> findByInfoAndStockCode(String info, String stockCode);

}
