package edu.pnu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String stockCode;
    private String title;
    private String writer;
    private String content;
}
