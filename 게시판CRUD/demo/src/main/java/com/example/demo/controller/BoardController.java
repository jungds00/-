package com.example.demo.controller;

import com.example.demo.dto.BoardResponseDTO;
import com.example.demo.model.Board;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    // 게시판 목록 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBoards(@RequestParam(defaultValue = "0") int offset,
                                                            @RequestParam(defaultValue = "20") int limit) {
        Page<BoardResponseDTO> boardPage = boardService.getAllBoards(offset, limit);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("value", boardPage.getContent());
        response.put("count", boardPage.getNumberOfElements());
        response.put("offset", offset);
        response.put("limit", limit);
        response.put("total", boardPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    // 게시판 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDTO> getBoardById(@PathVariable Long boardId) {
        BoardResponseDTO board = boardService.getBoardById(boardId);
        if(board == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(board);
    }

    // 게시판 생성
    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody Board board) {
        Board createdBoard = boardService.createBoard(board);
        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }

    // 게시판 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<Board> updateBoard(@PathVariable Long boardId, @RequestBody Board board) {
        Board updatedBoard = boardService.updateBoard(boardId, board);
        if (updatedBoard == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
    }

    // 게시판 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}