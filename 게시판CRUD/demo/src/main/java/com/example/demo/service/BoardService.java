package com.example.demo.service;

import com.example.demo.dto.BoardResponseDTO;
import com.example.demo.model.Board;
import com.example.demo.model.Comment;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 모든 게시판 가져오기
    public Page<BoardResponseDTO> getAllBoards(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Board> boardsPage = boardRepository.findAll(pageable);

        return boardsPage.map(board -> new BoardResponseDTO(
                board.getBoardId(),
                board.getDisplayName(),
                board.getBoardType(),
                board.getIsFavorite(),
                board.getOrderNo()
        ));
    }

    // 특정 게시판 아이디로 게시판 가져오기
    public BoardResponseDTO getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            return null;
        }

        return new BoardResponseDTO(
                board.getBoardId(),
                board.getDisplayName(),
                board.getBoardType(),
                board.getIsFavorite(),
                board.getOrderNo()
        );
    }
    // 게시판 생성
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    // 게시판 수정
    public Board updateBoard(Long boardId, Board updatedBoard) {

        return boardRepository.findById(boardId)
                .map(board -> {
                    board.setDisplayName(updatedBoard.getDisplayName());
                    board.setBoardType(updatedBoard.getBoardType());
                    board.setIsFavorite(updatedBoard.getIsFavorite());
                    board.setOrderNo(updatedBoard.getOrderNo());
                    return boardRepository.save(board);
                })
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다"));
    }
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        boardRepository.deleteById(id);
    }

}