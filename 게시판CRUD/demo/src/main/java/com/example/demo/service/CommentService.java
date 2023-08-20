package com.example.demo.service;

import com.example.demo.dto.CommentResponseDTO;
import com.example.demo.dto.WriterDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 게시글의 모든 댓글 가져오기
    public Page<CommentResponseDTO> getAllComments(Long postId, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Comment> commentsPage = commentRepository.findAllByPostPostId(postId,pageable);

        return commentsPage.map(comment -> {
            WriterDTO writerDTO = new WriterDTO();
            writerDTO.setDisplayName(comment.getWriter().getDisplayName());

            return new CommentResponseDTO(
                    comment.getCommentId(),
                    writerDTO,
                    comment.getContents(),
                    comment.getCreatedDateTime()
            );
        });
    }

    // 게시글의 댓글 상세 조회
    public CommentResponseDTO getCommentById(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElse(null);

        if (comment == null || !comment.getPost().getPostId().equals(postId)) {
            throw new RuntimeException("댓글을 찾을 수 없습니다.");
        }
        WriterDTO writerDTO = new WriterDTO();
        writerDTO.setDisplayName(comment.getWriter().getDisplayName());

        return new CommentResponseDTO(
                comment.getCommentId(),
                writerDTO,
                comment.getContents(),
                comment.getCreatedDateTime());
    }

    // 댓글 생성
    public Comment createComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    // 댓글 수정
    public Comment updateComment(Long postId, Long commentId, Comment updatedComment) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    if (!comment.getPost().getPostId().equals(postId)) {
                        throw new RuntimeException("알맞은 게시글이 아닙니다");
                    }
                    comment.setWriter(updatedComment.getWriter());
                    comment.setContents(updatedComment.getContents());
                    return commentRepository.save(comment);
                })
                .orElse(null);
    }

    // 댓글 삭제
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getPost().getPostId().equals(postId)) {
            throw new RuntimeException("알맞은 게시글이 아닙니다");
        }

        commentRepository.deleteById(commentId);
    }
}