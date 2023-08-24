package edu.pnu.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IncomeStatement {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no; // 각 데이터마다 고유한 ID가 필요하면 추가

    private String stockCode;
    private String itemCode;
    private long y2022;
    private long y2021;
    private long y2020;

    // 생성자, Getter, Setter 등 필요한 메서드들을 추가할 수 있습니다.
}
