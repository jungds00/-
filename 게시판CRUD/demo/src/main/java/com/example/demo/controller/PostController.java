package com.example.demo.controller;

import com.example.demo.dto.PostResponseDTO;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/boards/{boardId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(@PathVariable Long boardId,
                                                           @RequestParam(defaultValue = "0") int offset,
                                                           @RequestParam(defaultValue = "20") int limit) {
        Page<PostResponseDTO> postPage = postService.getAllPosts(boardId,offset, limit);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("value", postPage.getContent());
        response.put("count", postPage.getNumberOfElements());
        response.put("offset", offset);
        response.put("limit", limit);
        response.put("total", postPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    //

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long boardId,@PathVariable Long postId) {
        PostResponseDTO post = postService.getPostById(boardId,postId);
        if (post == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(post);
    }

    // 게시글 생성
    @PostMapping
    public ResponseEntity<Post> createPost(@PathVariable Long boardId,@RequestBody Post post) {
        Post createdPost = postService.createPost(boardId,post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long boardId,@PathVariable Long postId, @RequestBody Post post) {
        Post updatedPost = postService.updatePost(boardId,postId, post);
        if (updatedPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long boardId,@PathVariable Long postId) {
        postService.deletePost(boardId,postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}