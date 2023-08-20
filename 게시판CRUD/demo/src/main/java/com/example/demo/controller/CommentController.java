package com.example.demo.controller;

import com.example.demo.dto.CommentResponseDTO;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllComments(@PathVariable Long postId,
                                                              @RequestParam(defaultValue = "0") int offset,
                                                              @RequestParam(defaultValue = "20") int limit) {
        Page<CommentResponseDTO> commentPage = commentService.getAllComments(postId, offset, limit);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("value", commentPage.getContent());
        response.put("count", commentPage.getNumberOfElements());
        response.put("offset", offset);
        response.put("limit", limit);
        response.put("total", commentPage.getTotalElements());

        return ResponseEntity.ok(response);
    }


    // 댓글 상세 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long postId,@PathVariable Long commentId) {
        CommentResponseDTO comment = commentService.getCommentById(postId, commentId);

        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(comment);
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable Long postId, @RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(postId, comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody Comment comment) {
        Comment updatedComment = commentService.updateComment(postId, commentId, comment);
        if (updatedComment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}