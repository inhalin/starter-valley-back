package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.board.BoardRequestDto;
import startervalley.backend.dto.request.CommentRequestDto;
import startervalley.backend.dto.request.PageRequestDto;
import startervalley.backend.dto.board.BoardResponseDto;
import startervalley.backend.dto.response.CommentResponseDto;
import startervalley.backend.dto.response.PageResultDTO;
import startervalley.backend.entity.*;
import startervalley.backend.exception.NotOwnerException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.repository.BoardCommentRepository;
import startervalley.backend.repository.BoardRepository;
import startervalley.backend.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;

    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long userId, Long boardId) {
        User user = getUserElseThrow(userId);
        Board board = getBoardOrElseThrow(boardId);
        boolean own = Objects.equals(board.getUser().getId(), userId);
        int commentCount = boardCommentRepository.countByBoard(board);
        return BoardResponseDto.builder()
                .id(boardId)
                .profileUrl(user.getImageUrl())
                .title(board.getTitle())
                .content(board.getContent())
                .own(own)
                .name(board.getUser().getName())
                .commentCount(commentCount)
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public PageResultDTO<Board, BoardResponseDto> findBoardList(Long userID, PageRequestDto pageRequestDto) {
        Sort descByCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = pageRequestDto.getPageable(descByCreatedDate);
        Page<Board> boardPage = boardRepository.findAll(pageable);
        List<BoardResponseDto> dtoList = boardPage.getContent().stream()
                .map(b -> {
                    User findUser = b.getUser();
                    boolean own = Objects.equals(findUser.getId(), userID);
                    int commentCount = boardCommentRepository.countByBoard(b);
                    return BoardResponseDto.builder()
                            .id(b.getId())
                            .profileUrl(findUser.getImageUrl())
                            .title(b.getTitle())
                            .content(b.getContent())
                            .createdDate(b.getCreatedDate())
                            .modifiedDate(b.getModifiedDate())
                            .own(own)
                            .name(b.getUser().getName())
                            .commentCount(commentCount)
                            .build();
                }).toList();
        return new PageResultDTO<>(boardPage, dtoList);
    }

    public Long createBoard(Long userId, BoardRequestDto boardRequestDto) {
        User user = getUserElseThrow(userId);

        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getContent(), user);

        boardRepository.save(board);
        return board.getId();
    }

    public void updateBoard(Long userId, Long boardId, BoardRequestDto boardRequestDto) {

        Board board = getBoardOrElseThrow(boardId);

        if (!Objects.equals(board.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }

        board.update(boardRequestDto.getTitle(), boardRequestDto.getContent());
    }

    public void deleteBoard(Long userId, Long boardId) {

        Board board = getBoardOrElseThrow(boardId);

        if (!Objects.equals(board.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }
        List<BoardComment> commentList = boardCommentRepository.findAllByBoard(board);
        commentList.forEach(BoardComment::setNullBoard);
        boardRepository.deleteById(boardId);
    }

    public List<CommentResponseDto> findBoardCommentList(Long userId, Long boardId, PageRequestDto pageRequestDto) {
        User user = getUserElseThrow(userId);
        Board board = getBoardOrElseThrow(boardId);

        List<BoardComment> boardCommentList = boardCommentRepository.findAllByBoard(board);
        return boardCommentList.stream()
                .map(comment -> {
                    User commentUser = comment.getUser();
                    boolean own = Objects.equals(commentUser.getId(), user.getId());
                    return CommentResponseDto.builder()
                            .id(comment.getId())
                            .description(comment.getContent())
                            .author(commentUser.getName())
                            .isOwn(own)
                            .createdDate(comment.getCreatedDate())
                            .modifiedDate(comment.getModifiedDate())
                            .build();
                }).toList();
    }

    @Transactional
    public Long createComment(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
        User user = getUserElseThrow(userId);
        Board board = getBoardOrElseThrow(boardId);

        String content = commentRequestDto.getContent();
        BoardComment boardComment = BoardComment.builder()
                .user(user)
                .board(board)
                .content(content)
                .build();
        boardCommentRepository.save(boardComment);

        return boardComment.getId();
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        BoardComment boardComment = getBoardCommentOrElseThrow(commentId);

        if (!Objects.equals(boardComment.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }

        boardComment.update(commentRequestDto.getContent());
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        BoardComment boardComment = getBoardCommentOrElseThrow(commentId);

        if (!Objects.equals(boardComment.getUser().getId(), userId)) {
            throw new NotOwnerException();
        }

        boardCommentRepository.deleteById(commentId);
    }

    private Board getBoardOrElseThrow(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Board", "id", id.toString()));
    }

    private BoardComment getBoardCommentOrElseThrow(Long id) {
        return boardCommentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("BoardComment", "id", id.toString()));
    }
    
    private User getUserElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", String.valueOf(userId)));
    }
}
