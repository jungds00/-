package com.example.demo.service;

import com.example.demo.dto.PostResponseDTO;
import com.example.demo.dto.WriterDTO;
import com.example.demo.model.Board;
import com.example.demo.model.Post;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    // 게시판의 모든 게시글 가져오기
    public Page<PostResponseDTO> getAllPosts(Long boardId,int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postsPage = postRepository.findAllByBoardBoardId(boardId,pageable);

        return postsPage.map(post -> {
            String boardDisplayName = post.getBoard().getDisplayName();
            WriterDTO writerDTO = new WriterDTO();
            writerDTO.setDisplayName(post.getWriter().getDisplayName());
            int commentsCount = post.getComments().size();

            return new PostResponseDTO(
                    post.getPostId(),
                    post.getTitle(),
                    post.getBoard().getBoardId(),
                    boardDisplayName,
                    writerDTO,
                    post.getContents(),
                    post.getCreatedDateTime(),
                    commentsCount
            );
        });
    }
    // 게시글 상세 조회
    public PostResponseDTO getPostById(Long boardId,Long postId) {
        Post post = postRepository.findById(postId)
                .orElse(null);

        if (post == null || !post.getBoard().getBoardId().equals(boardId)) {
            return null;
        }

        String boardDisplayName = post.getBoard().getDisplayName();
        WriterDTO writerDTO = new WriterDTO();
        writerDTO.setDisplayName(post.getWriter().getDisplayName());
        int commentsCount = post.getComments().size();

        return new PostResponseDTO(
                post.getPostId(),
                post.getTitle(),
                post.getBoard().getBoardId(),
                boardDisplayName,
                writerDTO,
                post.getContents(),
                post.getCreatedDateTime(),
                commentsCount
        );
    }

    // 게시글 생성
    public Post createPost(Long boardId,Post post) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        post.setBoard(board);
        return postRepository.save(post);
    }

    // 게시글 수정
    public Post updatePost(Long boardId,Long postId, Post updatedPost) {
        return postRepository.findById(postId)
                .map(post -> {
                    if (!post.getBoard().getBoardId().equals(boardId)) {
                        throw new RuntimeException("알맞은 게시판이 아닙니다");
                    }
                    post.setTitle(updatedPost.getTitle());
                    post.setContents(updatedPost.getContents());
                    return postRepository.save(post);
                })
                .orElse(null);
    }

    // 게시글 삭제
    public void deletePost(Long boardId,Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        if (!post.getBoard().getBoardId().equals(boardId)) {
            throw new RuntimeException("알맞은 게시판이 아닙니다");
        }

        postRepository.deleteById(postId);
    }

}