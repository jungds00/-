package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDTO {

    private Long boardId;
    private String displayName;
    private String boardType;
    private Boolean isFavorite;
    private Integer orderNo;
}
