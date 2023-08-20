package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private Long postId;
    private String title;
    private Long boardId;
    private String boardDisplayName;
    private WriterDTO writer;
    private String contents;
    private LocalDateTime createdDateTime;
    private int commentsCount;
}