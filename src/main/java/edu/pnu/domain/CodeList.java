package edu.pnu.domain;

import jakarta.persistence.Entity;
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
public class CodeList {

    @Id
    private String primaryCode;
    private String info;
    private String bigOrSmall;
    private int code;
    private String stockCode;
    private String itemCode;
    private String itemName;
    private String itemName2;

    // 생성자, Getter, Setter 등 필요한 메서드들을 추가할 수 있습니다.
}
