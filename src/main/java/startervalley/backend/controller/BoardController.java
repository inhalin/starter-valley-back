package startervalley.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import startervalley.backend.dto.board.BoardRequestDto;
import startervalley.backend.dto.request.CommentRequestDto;
import startervalley.backend.dto.request.PageRequestDto;
import startervalley.backend.dto.board.BoardResponseDto;
import startervalley.backend.dto.response.CommentResponseDto;
import startervalley.backend.dto.response.PageResultDTO;
import startervalley.backend.entity.Board;
import startervalley.backend.entity.BoardComment;
import startervalley.backend.security.auth.CustomUserDetails;
import startervalley.backend.service.BoardService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/freeboards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("id") Long boardId) {

        Long userId = userDetails.getId();
        BoardResponseDto result = boardService.findBoard(userId, boardId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<PageResultDTO<Board, BoardResponseDto>> getBoardList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @Valid @ModelAttribute PageRequestDto pageRequestDto) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(boardService.findBoardList(userId, pageRequestDto));
    }

    @PostMapping
    public ResponseEntity<Long> createBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @Valid @RequestBody BoardRequestDto boardRequestDto) {
        Long userId = userDetails.getId();
        Long result = boardService.createBoard(userId, boardRequestDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @PathVariable("id") Long boardId,
                            @Valid @RequestBody BoardRequestDto boardRequestDto) {
        Long userId = userDetails.getId();
        boardService.updateBoard(userId, boardId, boardRequestDto);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                            @PathVariable("id") Long boardId) {
        Long userId = userDetails.getId();
        boardService.deleteBoard(userId, boardId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getBoardCommentList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable Long id,
                                                                        @Valid @ModelAttribute PageRequestDto pageRequestDto) {
        Long userId = userDetails.getId();
        List<CommentResponseDto> result = boardService.findBoardCommentList(userId, id, pageRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Long> createComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable("id") Long storeId,
                                              @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Long userId = userDetails.getId();
        Long result = boardService.createComment(userId, storeId, commentRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable("id") Long storeId,
                                              @Valid @RequestBody CommentRequestDto commentRequestDto) {
        Long userId = userDetails.getId();
        boardService.updateComment(userId, storeId, commentRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable("id") Long commentId) {
        Long userId = userDetails.getId();
        boardService.deleteComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
